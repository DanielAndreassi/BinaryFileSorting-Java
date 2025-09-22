import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class Arquivo {
    private RandomAccessFile arquivo;

    private int comp, mov;

    public Arquivo(String NomeArquivo) {
        try {
            arquivo = new RandomAccessFile(NomeArquivo, "rw");
        } catch (IOException ignored) {
        }
    }

    public Arquivo() {
    }

    public void close() throws IOException {
        arquivo.close();
    }

    public void initComp() {
        this.comp = 0;
    }

    public void initMov() {
        this.mov = 0;
    }

    public int getComp() {
        return this.comp;
    }

    public int getMov() {
        return this.mov;
    }

    public RandomAccessFile getFile() {
        return this.arquivo;
    }

    public void truncate(long pos) {
        try {
            arquivo.setLength(pos * Registro.length());
        } catch (IOException ignored) {
        }
    }

    public boolean eof() {
        boolean retorno = false;
        try {
            if (arquivo.getFilePointer() == arquivo.length())
                retorno = true;

        } catch (IOException ignored) {
        }
        return retorno;
    }

    private void inserirNoFinal(Registro registro) {
        seekArq(filesize());
        registro.gravaNoArq(arquivo);
        mov++;
    }

    public int filesize() {
        try {
            if (arquivo.length() > 0)
                return (int) (arquivo.length() / Registro.length());
            return 0;
        } catch (IOException ignored) {
        }
        return -1;
    }

    public void seekArq(int pos) {
        try {
            arquivo.seek(pos * Registro.length());
        } catch (IOException ignored) {
        }
    }

    // ========================= SORTING ===================================

    public void insercaoDireta() {
        int pos, TL = filesize();
        Registro regAtual = new Registro();
        Registro regAnt = new Registro();

        for (int i = 1; i < TL; i++) {
            seekArq(i);
            regAtual.leDoArq(arquivo);

            pos = i;
            seekArq(pos - 1);
            regAnt.leDoArq(arquivo);

            while (pos > 0 && regAtual.getCodigo() < regAnt.getCodigo()) {
                comp++;

                seekArq(pos);
                regAnt.gravaNoArq(arquivo);
                mov++;

                pos--;

                if (pos > 0) {
                    seekArq(pos - 1);
                    regAnt.leDoArq(arquivo);
                }

            }

            if (pos > 0)
                comp++;

            seekArq(pos);
            regAtual.gravaNoArq(arquivo);
            mov++;
        }

    }

    private int buscaBinaria(int codigo, int fim) {
        int ini = 0, meio = fim / 2;
        Registro regMeio = new Registro();

        seekArq(meio);
        regMeio.leDoArq(arquivo);

        comp++;
        while (ini <= fim && codigo != regMeio.getCodigo()) {
            comp++;
            if (codigo < regMeio.getCodigo()) {
                fim = meio - 1;
            } else {
                ini = meio + 1;
            }
            meio = (ini + fim) / 2;
            seekArq(meio);
            regMeio.leDoArq(arquivo);
        }

        comp++;
        if (codigo == regMeio.getCodigo()) {
            return meio;
        }

        return meio + 1;
    }

    public void insercaoBinaria() {
        int j, pos, TL = filesize();
        Registro regAtual = new Registro();
        Registro regProx = new Registro();

        for (int i = 1; i < TL; i++) {
            seekArq(i);
            regAtual.leDoArq(arquivo);

            pos = buscaBinaria(regAtual.getCodigo(), i - 1);

            for (j = i; j > pos; j--) {
                seekArq(j - 1);
                regProx.leDoArq(arquivo);
                regProx.gravaNoArq(arquivo);
                mov++;
            }
            if (pos != i) {
                seekArq(pos);
                regAtual.gravaNoArq(arquivo);
                mov++;
            }
        }
    }

    public void selecaoDireta() {
        int menor, j, TL = filesize();
        Registro regAtual = new Registro();
        Registro regProx = new Registro();
        Registro regMenor = new Registro();

        for (int i = 0; i < TL; i++) {
            menor = i;
            seekArq(i);
            regMenor.leDoArq(arquivo);

            for (j = i + 1; j < TL; j++) {
                seekArq(j);
                regProx.leDoArq(arquivo);

                comp++;
                if (regProx.getCodigo() < regMenor.getCodigo()) {
                    menor = j;
                    regMenor.setCodigo(regProx.getCodigo());
                }
            }

            if (menor != i) {
                seekArq(i);
                regAtual.leDoArq(arquivo);

                seekArq(i);
                regMenor.gravaNoArq(arquivo);

                seekArq(menor);
                regAtual.gravaNoArq(arquivo);

                mov += 2;
            }
        }
    }

    public void bubbleSort() {
        int i, TL = filesize();
        Registro regAtual = new Registro();
        Registro regProx = new Registro();
        boolean flag = true;

        while (TL > 1 && flag) {
            flag = false;

            for (i = 0; i < TL - 1; i++) {
                seekArq(i);
                regAtual.leDoArq(arquivo);
                regProx.leDoArq(arquivo);

                comp++;
                if (regAtual.getCodigo() > regProx.getCodigo()) {
                    seekArq(i);
                    regProx.gravaNoArq(arquivo);
                    regAtual.gravaNoArq(arquivo);
                    mov += 2;

                    flag = true;
                }
            }
            TL--;
        }
    }

    public void heapSort() {
        int pai, FD, FE, maiorF, TL = filesize();
        Registro regAtual = new Registro();
        Registro regProx = new Registro();

        while (TL > 1) {
            for (pai = TL / 2 - 1; pai >= 0; pai--) {
                FE = 2 * pai + 1;
                FD = FE + 1;
                maiorF = FE;

                if (FD < TL) {
                    seekArq(FE);
                    regAtual.leDoArq(arquivo);
                    regProx.leDoArq(arquivo);

                    comp++;
                    if (regProx.getCodigo() > regAtual.getCodigo())
                        maiorF = FD;

                }

                seekArq(pai);
                regAtual.leDoArq(arquivo);

                seekArq(maiorF);
                regProx.leDoArq(arquivo);

                comp++;
                if (regProx.getCodigo() > regAtual.getCodigo()) {
                    seekArq(maiorF);
                    regAtual.gravaNoArq(arquivo);

                    seekArq(pai);
                    regProx.gravaNoArq(arquivo);
                    mov += 2;
                }

            }

            seekArq(0);
            regAtual.leDoArq(arquivo);

            seekArq(TL - 1);
            regProx.leDoArq(arquivo);

            seekArq(0);
            regProx.gravaNoArq(arquivo);

            seekArq(TL - 1);
            regAtual.gravaNoArq(arquivo);
            mov += 2;

            TL--;
        }
    }

    public void shakeSort() {
        int i, ini = 0, fim = filesize() - 1;
        Registro regAtual = new Registro();
        Registro regAux = new Registro();
        boolean flag = true;

        while (ini < fim && flag) {
            flag = false;
            for (i = ini; i < fim; i++) {
                seekArq(i);
                regAtual.leDoArq(arquivo);
                regAux.leDoArq(arquivo);

                comp++;
                if (regAtual.getCodigo() > regAux.getCodigo()) {
                    seekArq(i);
                    regAux.gravaNoArq(arquivo);
                    regAtual.gravaNoArq(arquivo);
                    mov += 2;

                    flag = true;
                }
            }
            fim--;
            if (flag) {
                flag = false;
                for (i = fim; i > ini; i--) {
                    seekArq(i - 1);
                    regAux.leDoArq(arquivo);
                    regAtual.leDoArq(arquivo);

                    comp++;
                    if (regAtual.getCodigo() < regAux.getCodigo()) {
                        seekArq(i - 1);
                        regAtual.gravaNoArq(arquivo);
                        regAux.gravaNoArq(arquivo);
                        mov += 2;

                        flag = true;
                    }
                }
                ini++;
            }
        }
    }

    public void shellSort() {
        int i, pos, dist = 1, TL = filesize();
        Registro regAtual = new Registro();
        Registro regDist = new Registro();

        while (dist < TL)
            dist = dist * 3 + 1;

        while (dist > 0) {
            for (i = dist; i < TL; i++) {
                seekArq(i);
                regAtual.leDoArq(arquivo);

                pos = i;
                seekArq(pos - dist);
                regDist.leDoArq(arquivo);

                comp++;
                while (pos >= dist && regAtual.getCodigo() < regDist.getCodigo()) {
                    seekArq(pos);
                    regDist.gravaNoArq(arquivo);
                    mov++;

                    pos = pos - dist;

                    comp++;
                    if (pos >= dist) {
                        seekArq(pos - dist);
                        regDist.leDoArq(arquivo);
                    }
                }

                seekArq(pos);
                regAtual.gravaNoArq(arquivo);
                mov++;
            }
            dist = dist / 3;
        }
    }

    public void quickSortComPivo() {
        quickcp(0, filesize() - 1);
    }

    private void quickcp(int ini, int fim) {
        int i = ini, j = fim, pivo;
        Registro regI = new Registro();
        Registro regJ = new Registro();

        seekArq((ini + fim) / 2);
        regI.leDoArq(arquivo);
        pivo = regI.getCodigo();

        while (i <= j) {
            seekArq(i);
            regI.leDoArq(arquivo);
            if (i < fim) {
                comp++;
                while (regI.getCodigo() < pivo) {
                    i++;
                    seekArq(i);
                    regI.leDoArq(arquivo);
                }
            }

            seekArq(j);
            regJ.leDoArq(arquivo);
            if (j > ini) {
                comp++;
                while (regJ.getCodigo() > pivo) {
                    j--;
                    seekArq(j);
                    regJ.leDoArq(arquivo);
                }
            }

            if (i <= j) {
                seekArq(i);
                regJ.gravaNoArq(arquivo);
                seekArq(j);
                regI.gravaNoArq(arquivo);

                mov += 2;

                i++;
                j--;
            }
        }

        if (ini < j) {
            quickcp(ini, j);
        }
        if (i < fim) {
            quickcp(i, fim);
        }
    }

    public void quickSortSemPivo() {
        quickSP(0, filesize() - 1);
    }

    private void quickSP(int ini, int fim) {
        int i = ini, j = fim;
        boolean flag = true;
        Registro regI = new Registro();
        Registro regJ = new Registro();

        while (i < j) {
            seekArq(i);
            regI.leDoArq(arquivo);

            seekArq(j);
            regJ.leDoArq(arquivo);

            if (flag) {
                comp++;
                while (i < j && regI.getCodigo() <= regJ.getCodigo()) {
                    i++;
                    seekArq(i);
                    regI.leDoArq(arquivo);
                }
                if(i < j){
                    comp++;
                }
            } else {
                comp++;
                while (i < j && regJ.getCodigo() >= regI.getCodigo()) {
                    j--;
                    seekArq(j);
                    regJ.leDoArq(arquivo);
                }
                if(i < j){
                    comp++;
                }
            }

            if (i != j) {
                seekArq(i);
                regJ.gravaNoArq(arquivo);

                seekArq(j);
                regI.gravaNoArq(arquivo);

                mov += 2;
            }
            flag = !flag;
        }

        if (ini < i - 1)
            quickSP(ini, i - 1);
        if (j + 1 < fim)
            quickSP(j + 1, fim);
    }

    // ==================== SEARCHED SORTINGS ============================

    public void countingSort() throws IOException {
        int TL = filesize(), maior = maiorCodigo();
        int[] vet = new int[maior + 1];
        Registro reg = new Registro();
        Arquivo arqCopia = new Arquivo("counting.dat");

        seekArq(0);
        for (int i = 0; i < TL; i++) {
            reg.leDoArq(arquivo);
            vet[reg.getCodigo()]++;
        }

        for (int i = 1; i < vet.length; i++) {
            vet[i] += vet[i - 1];
        }

        copiarArquivo(arqCopia);

        for (int i = TL - 1; i >= 0; i--) {
            arqCopia.seekArq(i);
            reg.leDoArq(arqCopia.getFile());

            seekArq(vet[reg.getCodigo()] - 1);
            reg.gravaNoArq(arquivo);
            mov++;

            vet[reg.getCodigo()]--;
        }
    }

    public void combSort() {
        int TL = filesize(), interval = TL, i;
        Registro regAtual = new Registro();
        Registro regInterval = new Registro();
        boolean flag = true;

        while (interval > 1 || flag) {
            flag = false;

            interval = (interval * 10) / 13;
            if (interval < 1)
                interval = 1;

            for (i = 0; i < TL - interval; i++) {
                seekArq(i);
                regAtual.leDoArq(arquivo);

                seekArq(i + interval);
                regInterval.leDoArq(arquivo);

                comp++;
                if (regAtual.getCodigo() > regInterval.getCodigo()) {
                    seekArq(i);
                    regInterval.gravaNoArq(arquivo);

                    seekArq(i + interval);
                    regAtual.gravaNoArq(arquivo);
                    mov += 2;

                    flag = true;
                }
            }
        }
    }

    public void gnomeSort() {
        int i = 1, TL = filesize();
        Registro regAtual = new Registro();
        Registro regAnt = new Registro();

        while (i < TL) {
            seekArq(i);
            regAtual.leDoArq(arquivo);

            seekArq(i - 1);
            regAnt.leDoArq(arquivo);

            comp++;
            if (regAtual.getCodigo() >= regAnt.getCodigo()) {
                i++;
            } else {
                seekArq(i - 1);
                regAtual.gravaNoArq(arquivo);
                regAnt.gravaNoArq(arquivo);
                mov += 2;

                i--;
                if (i < 1)
                    i = 1;
            }
        }
    }

    // ========================= UTILS ====================================

    public void gerarArquivoRandomico(int tamanho) {
        arquivo = new Arquivo("random.dat").getFile();
        Registro reg = new Registro();
        truncate(0);
        Random rand = new Random();
        for (int i = 0; i < tamanho; i++) {
            reg.setCodigo(rand.nextInt(2000) + 1);
            inserirNoFinal(reg);
        }
    }

    public void gerarArquivoOrdenado(int tamanho) {
        arquivo = new Arquivo("ordenado.dat").getFile();
        Registro reg = new Registro();
        truncate(0);

        for (int i = 1; i <= tamanho; i++) {
            reg.setCodigo(i);
            inserirNoFinal(reg);
        }
    }

    public void gerarArquivoReverso(int tamanho) {
        arquivo = new Arquivo("reverso.dat").getFile();
        Registro reg = new Registro();
        truncate(0);

        for (int i = tamanho; i >= 1; i--) {
            reg.setCodigo(i);
            inserirNoFinal(reg);
        }
    }

    public void validaOrdenacao(String msg, int tamanho) {
        Registro reg1 = new Registro();
        Registro reg2 = new Registro();
        boolean erro = false;
        for (int i = 0; i <= tamanho; i++) {
            seekArq(i);
            reg1.leDoArq(arquivo);
            reg2.leDoArq(arquivo);
            if (reg1.getCodigo() > reg2.getCodigo()) {
                System.out.println(msg + " erro");
                erro = true;
                i = filesize() + 1;
            }
        }
        if (!erro)
            System.out.println(msg + " ok");
    }

    // ========================= AUXILIARS ====================================

    public void copiarArquivo(Arquivo arq) throws IOException {

        if (arq.arquivo == null) {
            arq.arquivo = new RandomAccessFile("copia.dat", "rw");
        }

        arq.initComp();
        arq.initMov();
        arq.truncate(0);

        seekArq(0);
        while (!eof()) {
            Registro reg = new Registro();
            reg.leDoArq(this.arquivo);
            reg.gravaNoArq(arq.arquivo);
        }
    }

    private int maiorCodigo() {
        int TL = filesize(), maior;
        Registro reg = new Registro();

        seekArq(0);
        reg.leDoArq(arquivo);
        maior = reg.getCodigo();

        for (int i = 1; i < TL; i++) {
            reg.leDoArq(arquivo);

            comp++;
            if (reg.getCodigo() > maior)
                maior = reg.getCodigo();
        }
        return maior;
    }
}