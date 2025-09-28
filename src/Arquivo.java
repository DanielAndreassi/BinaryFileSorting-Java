import java.io.File;
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

    // ========================= ORDENACOES ===================================

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
                if (i < j) {
                    comp++;
                }
            } else {
                comp++;
                while (i < j && regJ.getCodigo() >= regI.getCodigo()) {
                    j--;
                    seekArq(j);
                    regJ.leDoArq(arquivo);
                }
                if (i < j) {
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

    // ==================== PESQUISADOS ============================

    public void countingSort() throws IOException {
        int TL = filesize(), maior = maiorCodigo(), pos;
        int[] vet = new int[maior + 1];
        Registro reg = new Registro();
        Arquivo arqAux = new Arquivo("counting.dat");

        seekArq(0);
        for (int i = 0; i < TL; i++) {
            seekArq(i);
            reg.leDoArq(arquivo);
            vet[reg.getCodigo()]++;
        }

        for (int i = 1; i < vet.length; i++) {
            vet[i] += vet[i - 1];
        }

        copiarArquivo(arqAux);

        for (int i = TL - 1; i >= 0; i--) {
            arqAux.seekArq(i);
            reg.leDoArq(arqAux.getFile());

            pos = vet[reg.getCodigo()] - 1;

            seekArq(pos);
            reg.gravaNoArq(arquivo);
            mov++;

            vet[reg.getCodigo()]--;
        }

        deletarArquivo("counting.dat");
    }

    public void bucketSort() throws IOException {
        int i, j, pos, tam, TL = filesize(), k = 10;
        int menor = menorCodigo(), maior = maiorCodigo();
        int interval = maior - menor;
        Arquivo[] buckets = new Arquivo[k];
        Registro reg = new Registro();

        for (i = 0; i < k; i++) {
            buckets[i] = new Arquivo("bucket" + i + ".dat");
        }

        seekArq(0);
        for (i = 0; i < TL; i++) {
            reg.leDoArq(arquivo);
            pos = (reg.getCodigo() - menor) * k / (interval + 1);
            if (pos >= k)
                pos = k - 1;
            buckets[pos].inserirNoFinal(reg);
        }

        truncate(0);
        for (i = 0; i < k; i++) {
            buckets[i].countingSort();

            tam = buckets[i].filesize();
            buckets[i].seekArq(0);

            for (j = 0; j < tam; j++) {
                reg.leDoArq(buckets[i].arquivo);
                reg.gravaNoArq(arquivo);
                mov++;
            }

            buckets[i].arquivo.close();
            buckets[i].deletarArquivo("bucket" + i + ".dat");
        }
    }

    public void radixSort() throws IOException {
        int maior = maiorCodigo();
        Arquivo arqAux = new Arquivo("radix_aux.dat");
        arqAux.truncate(0);

        for (int i = 1; maior / i > 0; i *= 10) {
            countingSortForRadix(arqAux, i);
        }
    }

    public void countingSortForRadix(Arquivo arqAux, int num) throws IOException {
        int TL = filesize(), pos, digito;
        int[] vet = new int[10];
        Registro reg = new Registro();

        for (int i = 0; i < TL; i++) {
            seekArq(i);
            reg.leDoArq(this.arquivo);
            digito = (reg.getCodigo() / num) % 10;
            vet[digito]++;
        }

        for (int i = 1; i < vet.length; i++) {
            vet[i] += vet[i - 1];
        }

        for (int i = TL - 1; i >= 0; i--) {
            seekArq(i);
            reg.leDoArq(this.arquivo);

            digito = (reg.getCodigo() / num) % 10;

            pos = vet[digito] - 1;

            arqAux.seekArq(pos);
            reg.gravaNoArq(arqAux.getFile());
            mov++;

            vet[digito]--;
        }

        arqAux.copiarArquivo(this);
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

    public void timSort() throws IOException {
        int run = 32, esq, meio, dir, TL = filesize();
        Arquivo aux = new Arquivo("tim.dat");

        for (int i = 0; i < TL; i += run) {
            if (i + run < TL)
                insercaoDiretaForTim(i, i + run);
            else
                insercaoDiretaForTim(i, TL);
        }

        for (int tam = run; tam < TL; tam *= 2) {
            for (esq = 0; esq < TL; esq += 2 * tam) {
                meio = esq + tam - 1;
                if (esq + 2 * tam < TL)
                    dir = esq + 2 * tam - 1;
                else
                    dir = TL - 1;
                if (meio < dir)
                    fusao(aux, esq, meio, meio + 1, dir);
            }
        }

        deletarArquivo("tim.dat");
    }

    public void insercaoDiretaForTim(int ini, int fim) {
        int pos, TL = fim;
        Registro regAtual = new Registro();
        Registro regAnt = new Registro();

        for (int i = ini + 1; i < TL; i++) {
            seekArq(i);
            regAtual.leDoArq(arquivo);

            pos = i;
            seekArq(pos - 1);
            regAnt.leDoArq(arquivo);

            while (pos > ini && regAtual.getCodigo() < regAnt.getCodigo()) {
                comp++;

                seekArq(pos);
                regAnt.gravaNoArq(arquivo);
                mov++;

                pos--;

                if (pos > ini) {
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

    // ========================= UTILITARIOS ====================================

    public void gerarArquivoRandomico(int tamanho) throws IOException {
        Registro reg = new Registro();
        Random rand = new Random();
        truncate(0);
        for (int i = 0; i < tamanho; i++) {
            reg.setCodigo(rand.nextInt(2000) + 1);
            inserirNoFinal(reg);
        }
    }

    public void gerarArquivoOrdenado(int tamanho) throws IOException {
        truncate(0);

        Registro reg = new Registro();

        for (int i = 1; i <= tamanho; i++) {
            reg.setCodigo(i);
            inserirNoFinal(reg);
        }
    }

    public void gerarArquivoReverso(int tamanho) throws IOException {
        truncate(0);

        Registro reg = new Registro();

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

    public void deletarArquivo(String nomeArquivo) {
        File arq = new File(nomeArquivo);
        arq.delete();
    }

    public void copiarArquivo(Arquivo arqDestino) throws IOException {
        arqDestino.truncate(0);

        Registro reg = new Registro();
        for (int i = 0; i < filesize(); i++) {
            seekArq(i);
            reg.leDoArq(this.arquivo);

            arqDestino.seekArq(i);
            reg.gravaNoArq(arqDestino.getFile());
        }
    }

    public void exibir() {
        Registro reg = new Registro();
        seekArq(0);
        for (int i = 0; i < filesize(); i++) {
            reg.leDoArq(getFile());
            System.out.print(reg.getCodigo() + " ");
        }
        System.out.println();
    }

    // ======================== EXTRAS ====================================

    public void fusao(Arquivo arq, int ini1, int fim1, int ini2, int fim2) {
        int i = ini1, j = ini2, k = 0;
        Registro regI = new Registro();
        Registro regJ = new Registro();
        arq.truncate(0);

        seekArq(i);
        regI.leDoArq(arquivo);
        seekArq(j);
        regJ.leDoArq(arquivo);

        while (i <= fim1 && j <= fim2) {
            comp++;
            if (regI.getCodigo() < regJ.getCodigo()) {
                arq.seekArq(k);
                regI.gravaNoArq(arq.arquivo);
                i++;
                mov++;

                if (i <= fim1) {
                    seekArq(i);
                    regI.leDoArq(arquivo);
                }
            } else {
                arq.seekArq(k);
                regJ.gravaNoArq(arq.arquivo);
                j++;
                mov++;

                if (j <= fim2) {
                    seekArq(j);
                    regJ.leDoArq(arquivo);
                }
            }
            k++;
        }

        while (i <= fim1) {
            arq.seekArq(k);
            regI.gravaNoArq(arq.arquivo);
            i++;
            k++;
            mov++;
            if (i <= fim1) {
                seekArq(i);
                regI.leDoArq(arquivo);
            }
        }

        while (j <= fim2) {
            arq.seekArq(k);
            regJ.gravaNoArq(arq.arquivo);
            j++;
            k++;
            mov++;
            if (j <= fim2) {
                seekArq(j);
                regJ.leDoArq(arquivo);
            }
        }

        for (int pos = 0; pos < k; pos++) {
            arq.seekArq(pos);
            regI.leDoArq(arq.arquivo);

            seekArq(ini1 + pos);
            regI.gravaNoArq(arquivo);
            mov++;
        }
    }

    private int maiorCodigo() {
        int TL = filesize();
        int maior = 0;
        Registro reg = new Registro();

        seekArq(0);
        for (int i = 0; i < TL; i++) {
            reg.leDoArq(arquivo);

            comp++;
            if (reg.getCodigo() > maior)
                maior = reg.getCodigo();
        }
        return maior;
    }

    private int menorCodigo() {
        int TL = filesize();
        int menor = 999999;
        Registro reg = new Registro();

        seekArq(0);
        for (int i = 0; i < TL; i++) {
            reg.leDoArq(arquivo);

            comp++;
            if (reg.getCodigo() < menor)
                menor = reg.getCodigo();
        }
        return menor;
    }
}