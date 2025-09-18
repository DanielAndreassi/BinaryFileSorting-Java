import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class Arquivo
{
    private String NomeArquivo;
    private RandomAccessFile arquivo;
    private int comp, mov;

    public Arquivo(String NomeArquivo) {
        try {
            this.NomeArquivo = NomeArquivo;
            arquivo = new RandomAccessFile(NomeArquivo, "rw");
        } catch (IOException ignored) {}
    }

    public Arquivo() {}

    public void close () throws IOException {
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
        } catch (IOException ignored) {}
    }

    public boolean eof() {
        boolean retorno = false;
        try {
            if (arquivo.getFilePointer() == arquivo.length())
                retorno = true;

        } catch (IOException ignored) {}
        return retorno;
    }

    public void inserirRegNoFinal(Registro registro) {
        seekArq(filesize());
        registro.gravaNoArq(arquivo); mov++;
    }

    public int filesize() {
        try {
            if(arquivo.length() > 0)
                return (int) (arquivo.length() / Registro.length());
            return 0;
        } catch (IOException ignored) {}
        return -1;
    }

    public void seekArq(int pos) {
        try {
            arquivo.seek(pos * Registro.length());
        } catch (IOException ignored) {}
    }

    public void insercaoDireta() {
        int i, pos, tl = filesize();
        Registro registro = new Registro();
        Registro registroAnterior = new Registro();

        for (i = 1; i < tl; i++) {
            pos = i;
            seekArq(pos-1);
            registroAnterior.leDoArq(arquivo);
            registro.leDoArq(arquivo);
            comp++;
            while (pos > 0 && registro.getCodigo() < registroAnterior.getCodigo()) {
                seekArq(pos);
                registroAnterior.gravaNoArq(arquivo); mov++;
                pos--;
                if (pos > 0) {
                    seekArq(pos - 1);
                    registroAnterior.leDoArq(arquivo);
                }
                comp++;
            }
            seekArq(pos);
            registro.gravaNoArq(arquivo); mov++;
            seekArq(++i);
            registro.leDoArq(arquivo);
        }

    }

    public void geraArquivoRandomico() {
        arquivo = new Arquivo("random.dat").getFile();
        Registro reg = new Registro();
        truncate(0);
        Random rand = new Random();
        for (int i = 0; i < 1024; i++) {
            reg.setCodigo(rand.nextInt(2000) + 1);
            inserirRegNoFinal(reg);
        }
    }

    public void geraArquivoOrdenado() {
        arquivo = new Arquivo("ordenado.dat").getFile();
        if (filesize() > 0)
            truncate(0);
        for (int i = 0; i < 1024; i++)
            inserirRegNoFinal(new Registro(i + 1));
    }

    public void geraArquivoReverso() {
        arquivo = new Arquivo("reverso.dat").getFile();
        if (filesize() > 0)
            truncate(0);
        for (int i = 1024; i > 0; i--)
            inserirRegNoFinal(new Registro(i));
    }

    public void copiaArquivo(RandomAccessFile arquivoOrigem) throws IOException {
        arquivo = new Arquivo("copia.dat").getFile();
        truncate(0);
        arquivoOrigem.seek(0);
        while (arquivoOrigem.getFilePointer() != arquivoOrigem.length()){
            arquivo.writeInt(arquivoOrigem.readInt());
            for(int i=0 ; i < 1022; i++)
                arquivo.writeChar(arquivoOrigem.readChar());
        }
    }

    public void validaOrdenacao(String msg){
        Registro reg1 = new Registro();
        Registro reg2 = new Registro();
        boolean erro = false;
        for (int i = 0; i <= 1024; i++) {
            seekArq(i);
            reg1.leDoArq(arquivo);
            reg2.leDoArq(arquivo);
            if (reg1.getCodigo() > reg2.getCodigo()) {
                System.out.println(msg + " erro");
                erro = true;
                i = filesize() + 1;
            }
        }
        if(!erro)
            System.out.println(msg + " ok");
    }
    //demais metodos de ordenacao
}