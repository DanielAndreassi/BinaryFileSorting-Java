import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Principal {
    Arquivo arqOrd, arqRev, arqRand, auxRev, auxRand;

    public Principal() {
        arqOrd = new Arquivo();
        arqRev = new Arquivo();
        arqRand = new Arquivo();
        auxRev = new Arquivo();
        auxRand = new Arquivo();
    }

    public void geraTabela() {
        arqOrd.geraArquivoOrdenado();
        arqRev.geraArquivoReverso();
        arqRand.geraArquivoRandomico();
        String nomeArquivoTabela = "TabelaDeEficiencia.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nomeArquivoTabela))) {
            //cabecalho da tabela
            bw.write("|¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯|¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯|¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯|¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯|\n");
            bw.write("| Métodos de Ordenação  |                               Arquivo Ordenado                                |                             Arquivo em Ordem Reversa                           |                               Arquivo Randômico                               |\n");
            bw.write("| ----------------------|-------------------------------------------------------------------------------|--------------------------------------------------------------------------------|-------------------------------------------------------------------------------|\n");
            bw.write("| Nomes dos Métodos     | Comp. Prog. * | Comp. Equa. # | Mov. Prog. + | Mov. Equa. - |      Tempo      | Comp. Prog. * | Comp. Equa. # | Mov. Prog. + | Mov. Equa. - |      Tempo       | Comp. Prog. * | Comp. Equa. # | Mov. Prog. + | Mov. Equa. - |      Tempo      |\n");
            bw.write("|-----------------------|---------------|---------------|--------------|--------------|-----------------|---------------|---------------|--------------|--------------|------------------|---------------|---------------|--------------|--------------|-----------------|\n");


            //metodos e corpo tabela
            //insersecao direta
            System.out.println("Executando Insercao Direta...");
            StringBuilder linha = new StringBuilder("| Inserção Direta       | ");
            arqOrd.initComp();
            arqOrd.initMov();
            long tempoInicio = System.currentTimeMillis(); //método para pegar a hora atual em milisegundos
            arqOrd.insercaoDireta();
            long tempoFinal = System.currentTimeMillis();
            appendFirstSection(linha, (tempoFinal - tempoInicio) / 1000, arqOrd, arqOrd.filesize() - 1, 3 * (arqOrd.filesize() - 1));

            auxRev.initMov();
            auxRev.initComp();
            auxRev.copiaArquivo(arqRev.getFile());
            tempoInicio = System.currentTimeMillis();
            auxRev.insercaoDireta();
            tempoFinal = System.currentTimeMillis();
            appendMiddleSection(linha, (tempoFinal - tempoInicio) / 1000, auxRev, (int) (Math.pow(arqOrd.filesize(), 2) + arqOrd.filesize() - 2) / 4, (int) (Math.pow(arqOrd.filesize(), 2) + arqOrd.filesize() * 9 - 10) / 4);

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tempoInicio = System.currentTimeMillis();
            auxRand.insercaoDireta();
            tempoFinal = System.currentTimeMillis();
            appendLastSection(linha, (tempoFinal - tempoInicio) / 1000, auxRand, (int) (Math.pow(arqOrd.filesize(), 2) + arqOrd.filesize() - 4) / 4, (int) (Math.pow(arqOrd.filesize(), 2) + arqOrd.filesize() * 3 - 4) / 4);

            bw.write(linha.toString());
            quebraLinhaTabela(bw);

            //usar acima como base, e fazer os outros metodos

        } catch (IOException ignored) {
        }
    }

    public static void quebraLinhaTabela(BufferedWriter writer) throws IOException {
        writer.write("|_______________________|_______________________________________________________________________________|________________________________________________________________________________|_______________________________________________________________________________|\n");
    }

    public static void appendFirstSection(StringBuilder line, long tempo, Arquivo arq, int compEqua, int movEqua) {
        line.append(arq.getComp());
        line.append(" ".repeat(Math.max(0, 39 - (line.length() - 1))));
        line.append("| ");

        line.append(compEqua);
        line.append(" ".repeat(Math.max(0, 55 - (line.length() - 1))));
        line.append("| ");

        line.append(arq.getMov());
        line.append(" ".repeat(Math.max(0, 70 - (line.length() - 1))));
        line.append("| ");

        line.append(movEqua);
        line.append(" ".repeat(Math.max(0, 85 - (line.length() - 1))));
        line.append("| ");

        line.append(tempo);
        line.append(" ".repeat(Math.max(0, 103 - (line.length() - 1))));
        line.append("| ");
    }

    public static void appendMiddleSection(StringBuilder line, long tempo, Arquivo arq, int compEqua, int movEqua) {
        line.append(arq.getComp());
        line.append(" ".repeat(Math.max(0, 119 - (line.length() - 1))));
        line.append("| ");

        line.append(compEqua);
        line.append(" ".repeat(Math.max(0, 135 - (line.length() - 1))));
        line.append("| ");

        line.append(arq.getMov());
        line.append(" ".repeat(Math.max(0, 150 - (line.length() - 1))));
        line.append("| ");

        line.append(movEqua);
        line.append(" ".repeat(Math.max(0, 165 - (line.length() - 1))));
        line.append("| ");

        line.append(tempo);
        line.append(" ".repeat(Math.max(0, 184 - (line.length() - 1))));
        line.append("| ");
    }

    public static void appendLastSection(StringBuilder line, long tempo, Arquivo arq, int compEqua, int movEqua) {
        line.append(arq.getComp());
        line.append(" ".repeat(Math.max(0, 200 - (line.length() - 1))));
        line.append("| ");

        line.append(compEqua);
        line.append(" ".repeat(Math.max(0, 216 - (line.length() - 1))));
        line.append("| ");

        line.append(arq.getMov());
        line.append(" ".repeat(Math.max(0, 231 - (line.length() - 1))));
        line.append("| ");

        line.append(movEqua);
        line.append(" ".repeat(Math.max(0, 246 - (line.length() - 1))));
        line.append("| ");

        line.append(tempo);
        line.append(" ".repeat(Math.max(0, 264 - (line.length() - 1))));
        line.append("|\n");
    }
}
