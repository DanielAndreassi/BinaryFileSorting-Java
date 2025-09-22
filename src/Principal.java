import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Principal {
    Arquivo arqOrd, arqRev, arqRand, auxRev, auxRand;

    public Principal() {
        arqOrd = new Arquivo();
        arqRev = new Arquivo();
        arqRand = new Arquivo();
        auxRev = new Arquivo("reverso.dat");
        auxRand = new Arquivo("random.dat");
    }

    public void geraTabela() {
        arqOrd.gerarArquivoOrdenado(8);
        arqRev.gerarArquivoReverso(8);
        arqRand.gerarArquivoRandomico(8);
        String nomeArquivoTabela = "TabelaDeEficiencia.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nomeArquivoTabela))) {
            // cabecalho da tabela
            bw.write(
                    "|¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯|¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯|¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯|¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯|\n");
            bw.write(
                    "| Métodos de Ordenação  |                               Arquivo Ordenado                                |                             Arquivo em Ordem Reversa                           |                               Arquivo Randômico                               |\n");
            bw.write(
                    "| ----------------------|-------------------------------------------------------------------------------|--------------------------------------------------------------------------------|-------------------------------------------------------------------------------|\n");
            bw.write(
                    "| Nomes dos Métodos     | Comp. Prog. * | Comp. Equa. # | Mov. Prog. + | Mov. Equa. - |      Tempo      | Comp. Prog. * | Comp. Equa. # | Mov. Prog. + | Mov. Equa. - |      Tempo       | Comp. Prog. * | Comp. Equa. # | Mov. Prog. + | Mov. Equa. - |      Tempo      |\n");
            bw.write(
                    "|-----------------------|---------------|---------------|--------------|--------------|-----------------|---------------|---------------|--------------|--------------|------------------|---------------|---------------|--------------|--------------|-----------------|\n");

            // Inserção Direta
            System.out.println("Inserção Direta");
            StringBuilder line = new StringBuilder("| Inserção Direta       | ");
            arqOrd.initComp();
            arqOrd.initMov();
            long tini = System.currentTimeMillis();
            arqOrd.insercaoDireta();
            long tfim = System.currentTimeMillis();
            appendFirstSection(line, (tfim - tini) / 1000, arqOrd, arqOrd.filesize() - 1,
                    3 * (arqOrd.filesize() - 1));

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiarArquivo(arqRev);
            tini = System.currentTimeMillis();
            auxRev.insercaoDireta();
            tfim = System.currentTimeMillis();
            appendMiddleSection(line, (tfim - tini) / 1000, auxRev,
                    (int) (Math.pow(arqOrd.filesize(), 2) + arqOrd.filesize() - 2) / 4,
                    (int) (Math.pow(arqOrd.filesize(), 2) + arqOrd.filesize() * 9 - 10) / 4);

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiarArquivo(arqRand);
            tini = System.currentTimeMillis();
            auxRand.insercaoDireta();
            tfim = System.currentTimeMillis();
            appendLastSection(line, (tfim - tini) / 1000, auxRand,
                    (int) (Math.pow(arqOrd.filesize(), 2) + arqOrd.filesize() - 4) / 4,
                    (int) (Math.pow(arqOrd.filesize(), 2) + arqOrd.filesize() * 3 - 4) / 4);

            bw.write(line.toString());
            quebraLinhaTabela(bw);

            // Inserção Binária
            System.out.println("Inserção Binária");
            line = new StringBuilder("| Inserção Binária      | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.insercaoBinaria();
            tfim = System.currentTimeMillis();
            int comp = (int) (arqOrd.filesize() * (Math.log(arqOrd.filesize()) - Math.log(Math.E) + 0.5));
            appendFirstSection(line, (tfim - tini) / 1000, arqOrd, comp, 3 * (arqOrd.filesize() - 1));

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiarArquivo(arqRev);
            tini = System.currentTimeMillis();
            auxRev.insercaoBinaria();
            tfim = System.currentTimeMillis();
            appendMiddleSection(line, (tfim - tini) / 1000, auxRev, comp,
                    (int) (Math.pow(arqOrd.filesize(), 2) + arqOrd.filesize() * 3 - 4) / 4);

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiarArquivo(arqRand);
            tini = System.currentTimeMillis();
            auxRand.insercaoBinaria();
            tfim = System.currentTimeMillis();
            appendLastSection(line, (tfim - tini) / 1000, auxRand, comp,
                    (int) (Math.pow(arqOrd.filesize(), 2) + arqOrd.filesize() * 3 - 4) / 4);

            bw.write(line.toString());
            quebraLinhaTabela(bw);

            // Seleção Direta
            System.out.println("Seleção Direta");
            line = new StringBuilder("| Seleção Direta        | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.selecaoDireta();
            tfim = System.currentTimeMillis();
            comp = (int) (Math.pow(arqOrd.filesize(), 2) - arqOrd.filesize()) / 2;
            appendFirstSection(line, (tfim - tini) / 1000, arqOrd, comp, 3 * (arqOrd.filesize() - 1));

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiarArquivo(arqRev);
            tini = System.currentTimeMillis();
            auxRev.selecaoDireta();
            tfim = System.currentTimeMillis();
            appendMiddleSection(line, (tfim - tini) / 1000, auxRev, comp,
                    (int) (arqRev.filesize() * (Math.log(arqRev.filesize()) * 0.577216)));

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiarArquivo(arqRand);
            tini = System.currentTimeMillis();
            auxRand.selecaoDireta();
            tfim = System.currentTimeMillis();
            appendLastSection(line, (tfim - tini) / 1000, auxRand, comp,
                    (int) Math.pow(arqRand.filesize(), 2) / 4 + 3 * (arqRand.filesize() - 1));

            bw.write(line.toString());
            quebraLinhaTabela(bw);

            // Bolha
            System.out.println("Bolha");
            line = new StringBuilder("| Bolha                 | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.bubbleSort();
            tfim = System.currentTimeMillis();
            appendFirstSection(line, (tfim - tini) / 1000, arqOrd, comp, 0);

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiarArquivo(arqRev);
            tini = System.currentTimeMillis();
            auxRev.bubbleSort();
            tfim = System.currentTimeMillis();
            appendMiddleSection(line, (tfim - tini) / 1000, auxRev, comp,
                    (int) (3 * (Math.pow(arqRev.filesize(), 2) - arqRev.filesize()) / 4));

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiarArquivo(arqRand);
            tini = System.currentTimeMillis();
            auxRand.bubbleSort();
            tfim = System.currentTimeMillis();
            appendLastSection(line, (tfim - tini) / 1000, auxRand, comp,
                    (int) (3 * (Math.pow(arqRev.filesize(), 2) - arqRev.filesize()) / 4));

            bw.write(line.toString());
            quebraLinhaTabela(bw);

            // Shake
            System.out.println("Shake");
            line = new StringBuilder("| Shake                 | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.shakeSort();
            tfim = System.currentTimeMillis();
            appendFirstSection(line, (tfim - tini) / 1000, arqOrd, comp, 0);

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiarArquivo(arqRev);
            tini = System.currentTimeMillis();
            auxRev.shakeSort();
            tfim = System.currentTimeMillis();
            appendMiddleSection(line, (tfim - tini) / 1000, auxRev, comp,
                    (int) (3 * (Math.pow(arqRev.filesize(), 2) - arqRev.filesize()) / 4));

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiarArquivo(arqRand);
            tini = System.currentTimeMillis();
            auxRand.shakeSort();
            tfim = System.currentTimeMillis();
            appendLastSection(line, (tfim - tini) / 1000, auxRand, comp,
                    (int) (3 * (Math.pow(arqRev.filesize(), 2) - arqRev.filesize()) / 4));

            bw.write(line.toString());
            quebraLinhaTabela(bw);

            // Shell
            System.out.println("Shell");
            line = new StringBuilder("| Shell                 | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.shellSort();
            tfim = System.currentTimeMillis();
            appendFirstSection(line, (tfim - tini) / 1000, arqOrd, 0, 0);

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiarArquivo(arqRev);
            tini = System.currentTimeMillis();
            auxRev.shellSort();
            tfim = System.currentTimeMillis();
            appendMiddleSection(line, (tfim - tini) / 1000, auxRev, 0, 0);

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiarArquivo(arqRand);
            tini = System.currentTimeMillis();
            auxRand.shellSort();
            tfim = System.currentTimeMillis();
            appendLastSection(line, (tfim - tini) / 1000, auxRand, 0, 0);

            bw.write(line.toString());
            quebraLinhaTabela(bw);

            // Heap
            System.out.println("Heap");
            line = new StringBuilder("| Heap                  | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.heapSort();
            tfim = System.currentTimeMillis();
            appendFirstSection(line, (tfim - tini) / 1000, arqOrd, 0, 0);

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiarArquivo(arqRev);
            tini = System.currentTimeMillis();
            auxRev.heapSort();
            tfim = System.currentTimeMillis();
            appendMiddleSection(line, (tfim - tini) / 1000, auxRev, 0, 0);

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiarArquivo(arqRand);
            tini = System.currentTimeMillis();
            auxRand.heapSort();
            tfim = System.currentTimeMillis();
            appendLastSection(line, (tfim - tini) / 1000, auxRand, 0, 0);

            bw.write(line.toString());
            quebraLinhaTabela(bw);

            // Quick sem pivo
            System.out.println("Quick Sem pivô");
            line = new StringBuilder("| Quick Sem pivô         | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.quickSortSemPivo();
            tfim = System.currentTimeMillis();
            appendFirstSection(line, (tfim - tini) / 1000, arqOrd, 0, 0);

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiarArquivo(arqRev);
            tini = System.currentTimeMillis();
            auxRev.quickSortSemPivo();
            tfim = System.currentTimeMillis();
            appendMiddleSection(line, (tfim - tini) / 1000, auxRev, 0, 0);

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiarArquivo(arqRand);
            tini = System.currentTimeMillis();
            auxRand.quickSortSemPivo();
            tfim = System.currentTimeMillis();
            appendLastSection(line, (tfim - tini) / 1000, auxRand, 0, 0);

            bw.write(line.toString());
            quebraLinhaTabela(bw);

            // Quick com pivo
            System.out.println("Quick Com pivô");
            line = new StringBuilder("| Quick Com pivô         | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.quickSortComPivo();
            tfim = System.currentTimeMillis();
            appendFirstSection(line, (tfim - tini) / 1000, arqOrd, 0, 0);

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiarArquivo(arqRev);
            tini = System.currentTimeMillis();
            auxRev.quickSortComPivo();
            tfim = System.currentTimeMillis();
            appendMiddleSection(line, (tfim - tini) / 1000, auxRev, 0, 0);

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiarArquivo(arqRand);
            tini = System.currentTimeMillis();
            auxRand.quickSortComPivo();
            tfim = System.currentTimeMillis();
            appendLastSection(line, (tfim - tini) / 1000, auxRand, 0, 0);

            // Counting
            System.out.println("Counting"); 
            line = new StringBuilder("| Counting              | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.countingSort();
            tfim = System.currentTimeMillis();
            appendFirstSection(line, (tfim - tini) / 1000, arqOrd, 0, 0);
 
            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiarArquivo(arqRev);
            tini = System.currentTimeMillis();
            auxRev.countingSort();
            tfim = System.currentTimeMillis();
            appendMiddleSection(line, (tfim - tini) / 1000, auxRev, 0, 0);
 
            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiarArquivo(arqRand);
            tini = System.currentTimeMillis();
            auxRand.countingSort();
            tfim = System.currentTimeMillis();
            appendLastSection(line, (tfim - tini) / 1000, auxRand, 0, 0);

            bw.write(line.toString());
            quebraLinhaTabela(bw);

            // Comb
            System.out.println("Comb");
            line = new StringBuilder("| Comb                  | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.combSort();
            tfim = System.currentTimeMillis();
            appendFirstSection(line, (tfim - tini) / 1000, arqOrd, 0, 0);

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiarArquivo(arqRev);
            tini = System.currentTimeMillis();
            auxRev.combSort();
            tfim = System.currentTimeMillis();
            appendMiddleSection(line, (tfim - tini) / 1000, auxRev, 0, 0);

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiarArquivo(arqRand);
            tini = System.currentTimeMillis();
            auxRand.combSort();
            tfim = System.currentTimeMillis();
            appendLastSection(line, (tfim - tini) / 1000, auxRand, 0, 0);

            bw.write(line.toString());
            quebraLinhaTabela(bw);

            // Bucket
            System.out.println("Bucket");
            line = new StringBuilder("| Bucket                 | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.bucketSort();
            tfim = System.currentTimeMillis();
            appendFirstSection(line, (tfim - tini) / 1000, arqOrd, 0, 0);

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiarArquivo(arqRev);
            tini = System.currentTimeMillis();
            auxRev.bucketSort();
            tfim = System.currentTimeMillis();
            appendMiddleSection(line, (tfim - tini) / 1000, auxRev, 0, 0);

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiarArquivo(arqRand);
            tini = System.currentTimeMillis();
            auxRand.bucketSort();
            tfim = System.currentTimeMillis();
            appendLastSection(line, (tfim - tini) / 1000, auxRand, 0, 0);

            bw.write(line.toString());
            quebraLinhaTabela(bw);

            // Gnome
            System.out.println("Gnome");
            line = new StringBuilder("| Gnome                 | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.gnomeSort();
            tfim = System.currentTimeMillis();
            appendFirstSection(line, (tfim - tini) / 1000, arqOrd, 0, 0);

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiarArquivo(arqRev);
            tini = System.currentTimeMillis();
            auxRev.gnomeSort();
            tfim = System.currentTimeMillis();
            appendMiddleSection(line, (tfim - tini) / 1000, auxRev, 0, 0);

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiarArquivo(arqRand);
            tini = System.currentTimeMillis();
            auxRand.gnomeSort();
            tfim = System.currentTimeMillis();
            appendLastSection(line, (tfim - tini) / 1000, auxRand, 0, 0);

            bw.write(line.toString());
            quebraLinhaTabela(bw); 

        } catch (IOException ignored) {
        }
    }

    public static void quebraLinhaTabela(BufferedWriter bw) throws IOException {
        bw.write(
                "|_______________________|_______________________________________________________________________________|________________________________________________________________________________|_______________________________________________________________________________|\n");
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