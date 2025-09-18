import java.io.IOException;
import java.io.RandomAccessFile;

class Registro
{
    private int codigo; //4 bytes
    public final int tf=1022;
    private final char[] lixo = new char[tf]; //2044 bytes

    public Registro(int codigo)
    {
        this.codigo = codigo;
        for (int i=0 ; i<tf ; i++)
            lixo[i]='X';
    }

    public Registro() {}

    public int getCodigo()
    {
        return(codigo);
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void gravaNoArq(RandomAccessFile arquivo)
    {
        try
        {
            arquivo.writeInt(codigo);
            for(int i=0 ; i<tf ; i++)
                arquivo.writeChar(lixo[i]);
        }catch(IOException e){}
    }

    public void leDoArq(RandomAccessFile arquivo)
    {
        try
        {
            codigo = arquivo.readInt();
            for(int i=0 ; i<tf ; i++)
                lixo[i]=arquivo.readChar();
        }catch(IOException e){}
    }

    static int length()
    {
        //int numero; 4 bytes
        //char lixo[] = new char[tf]; 2044 bytes
        //--------------------------------------
        // 2048 bytes
        return(2048);
    }
}