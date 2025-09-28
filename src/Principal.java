import java.io.IOException;
import java.io.RandomAccessFile;

public class Principal {
        Arquivo arqOrd, arqRev, arqRand, auxOrd, auxRev, auxRand;
        RandomAccessFile arquivo;
        long inicio, fim, compOrd, movOrd, compRev, movRev, compRand, movRand, tempoOrd, tempoRev,tempoRand;
        int totalReg = 128;

        public Principal() {
                arqOrd = new Arquivo("ordenado.dat");
                arqRev = new Arquivo("reverso.dat");
                arqRand = new Arquivo("random.dat");
                auxOrd = new Arquivo("aux_ordenado.dat");
                auxRev = new Arquivo("aux_reverso.dat");
                auxRand = new Arquivo("aux_random.dat");
        }

        public void escreverLinha(String ordenacao,
                        double compOrd, double compEOrd, double movOrd, double movEOrd, double tempoOrd,
                        double compRev, double compERev, double movRev, double movERev, double tempoRev,
                        double compRand, double compERand, double movRand, double movERand, double tempoRand) {

                String dados = String.format(
                                "|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|",
                                centralizarString(ordenacao, 26),
                                centralizarString(String.valueOf(compOrd), 10),
                                centralizarString(String.valueOf(compEOrd), 10),
                                centralizarString(String.valueOf(movOrd), 10),
                                centralizarString(String.valueOf(movEOrd), 10),
                                centralizarString(String.valueOf(tempoOrd), 10),
                                centralizarString(String.valueOf(compRev), 10),
                                centralizarString(String.valueOf(compERev), 10),
                                centralizarString(String.valueOf(movRev), 10),
                                centralizarString(String.valueOf(movERev), 10),
                                centralizarString(String.valueOf(tempoRev), 10),
                                centralizarString(String.valueOf(compRand), 10),
                                centralizarString(String.valueOf(compERand), 10),
                                centralizarString(String.valueOf(movRand), 10),
                                centralizarString(String.valueOf(movERand), 10),
                                centralizarString(String.valueOf(tempoRand), 10));

                String linha = String.format(
                                """
                                                |                          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |
                                                %s
                                                |                          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |
                                                +--------------------------|----------|----------|----------|----------|----------+----------|----------|----------|----------|----------+----------|----------|----------|----------|----------|
                                                """,
                                dados);

                gravaStringNoArquivo(linha);
        }

        public void gerarTabela() throws IOException {
                try {
                        arquivo = new RandomAccessFile("TabelaDeEficiencia.txt", "rw");
                        arquivo.setLength(0);
                } catch (Exception ignored) {
                }

                escreverCabecalho();

                arqOrd.gerarArquivoOrdenado(totalReg);
                arqRev.gerarArquivoReverso(totalReg);
                arqRand.gerarArquivoRandomico(totalReg);

                double compEOrd, movEOrd, compERev, movERev, compERand, movERand;

                // =================== INSERCAO DIRETA ===================
                System.out.println("INSERCAO DIRETA...");

                arqOrd.copiarArquivo(auxOrd);
                auxOrd.initComp();
                auxOrd.initMov();
                inicio = System.currentTimeMillis();
                auxOrd.insercaoDireta();
                fim = System.currentTimeMillis();
                tempoOrd = (fim - inicio) / 1000;
                compOrd = auxOrd.getComp();
                movOrd = auxOrd.getMov();
                compEOrd = totalReg - 1;
                movEOrd = 3 * (totalReg - 1);

                arqRev.copiarArquivo(auxRev);
                auxRev.initComp();
                auxRev.initMov();
                inicio = System.currentTimeMillis();
                auxRev.insercaoDireta();
                fim = System.currentTimeMillis();
                tempoRev = (fim - inicio) / 1000;
                compRev = auxRev.getComp();
                movRev = auxRev.getMov();
                compERev = (totalReg * totalReg + totalReg - 4) / 4;
                movERev = (totalReg * totalReg + 3 * totalReg - 4) / 2;

                arqRand.copiarArquivo(auxRand);
                auxRand.initComp();
                auxRand.initMov();
                inicio = System.currentTimeMillis();
                auxRand.insercaoDireta();
                fim = System.currentTimeMillis();
                tempoRand = (fim - inicio) / 1000;
                compRand = auxRand.getComp();
                movRand = auxRand.getMov();
                compERand = (totalReg * totalReg + totalReg - 2) / 4;
                movERand = (totalReg * totalReg + 9 * totalReg - 10) / 4;

                escreverLinha("Insercao Direta",
                                compOrd, compEOrd, movOrd, movEOrd, tempoOrd,
                                compRev, compERev, movRev, movERev, tempoRev,
                                compRand, compERand, movRand, movERand, tempoRand);

                // =================== SELECAO DIRETA ===================
                System.out.println("SELECAO DIRETA...");

                arqOrd.copiarArquivo(auxOrd);
                auxOrd.initComp();
                auxOrd.initMov();
                inicio = System.currentTimeMillis();
                auxOrd.selecaoDireta();
                fim = System.currentTimeMillis();
                tempoOrd = (fim - inicio) / 1000;
                compOrd = auxOrd.getComp();
                movOrd = auxOrd.getMov();
                compEOrd = (totalReg * totalReg - totalReg) / 2;
                movEOrd = 3 * (totalReg - 1);

                arqRev.copiarArquivo(auxRev);
                auxRev.initComp();
                auxRev.initMov();
                inicio = System.currentTimeMillis();
                auxRev.selecaoDireta();
                fim = System.currentTimeMillis();
                tempoRev = (fim - inicio) / 1000;
                compRev = auxRev.getComp();
                movRev = auxRev.getMov();
                compERev = (totalReg * totalReg - totalReg) / 2;
                movERev = 3 * (totalReg - 1);

                arqRand.copiarArquivo(auxRand);
                auxRand.initComp();
                auxRand.initMov();
                inicio = System.currentTimeMillis();
                auxRand.selecaoDireta();
                fim = System.currentTimeMillis();
                tempoRand = (fim - inicio) / 1000;
                compRand = auxRand.getComp();
                movRand = auxRand.getMov();
                compERand = (totalReg * totalReg - totalReg) / 2;
                movERand = 3 * (totalReg - 1);

                escreverLinha("Selecao Direta",
                                compOrd, compEOrd, movOrd, movEOrd, tempoOrd,
                                compRev, compERev, movRev, movERev, tempoRev,
                                compRand, compERand, movRand, movERand, tempoRand);

                // =================== INSERCAO BINARIA ===================
                System.out.println("INSERCAO BINARIA...");

                arqOrd.copiarArquivo(auxOrd);
                auxOrd.initComp();
                auxOrd.initMov();
                inicio = System.currentTimeMillis();
                auxOrd.insercaoBinaria();
                fim = System.currentTimeMillis();
                tempoOrd = (fim - inicio) / 1000;
                compOrd = auxOrd.getComp();
                movOrd = auxOrd.getMov();
                compEOrd = (int) (totalReg * (Math.log(totalReg) / Math.log(2)));
                movEOrd = 3 * (totalReg - 1);

                arqRev.copiarArquivo(auxRev);
                auxRev.initComp();
                auxRev.initMov();
                inicio = System.currentTimeMillis();
                auxRev.insercaoBinaria();
                fim = System.currentTimeMillis();
                tempoRev = (fim - inicio) / 1000;
                compRev = auxRev.getComp();
                movRev = auxRev.getMov();
                compERev = (int) (totalReg * (Math.log(totalReg) / Math.log(2)));
                movERev = (totalReg * totalReg + 3 * totalReg - 4) / 2;

                arqRand.copiarArquivo(auxRand);
                auxRand.initComp();
                auxRand.initMov();
                inicio = System.currentTimeMillis();
                auxRand.insercaoBinaria();
                fim = System.currentTimeMillis();
                tempoRand = (fim - inicio) / 1000;
                compRand = auxRand.getComp();
                movRand = auxRand.getMov();
                compERand = (int) (totalReg * (Math.log(totalReg) / Math.log(2)));
                movERand = (totalReg * totalReg + 9 * totalReg - 10) / 4;

                escreverLinha("Insercao Binaria",
                                compOrd, compEOrd, movOrd, movEOrd, tempoOrd,
                                compRev, compERev, movRev, movERev, tempoRev,
                                compRand, compERand, movRand, movERand, tempoRand);

                // =================== BUBBLE SORT ===================
                System.out.println("BUBBLE SORT...");

                arqOrd.copiarArquivo(auxOrd);
                auxOrd.initComp();
                auxOrd.initMov();
                inicio = System.currentTimeMillis();
                auxOrd.bubbleSort();
                fim = System.currentTimeMillis();
                tempoOrd = (fim - inicio) / 1000;
                compOrd = auxOrd.getComp();
                movOrd = auxOrd.getMov();
                compEOrd = (totalReg * totalReg - totalReg) / 2;
                movEOrd = 0;

                arqRev.copiarArquivo(auxRev);
                auxRev.initComp();
                auxRev.initMov();
                inicio = System.currentTimeMillis();
                auxRev.bubbleSort();
                fim = System.currentTimeMillis();
                tempoRev = (fim - inicio) / 1000;
                compRev = auxRev.getComp();
                movRev = auxRev.getMov();
                compERev = (totalReg * totalReg - totalReg) / 2;
                movERev = 3 * (totalReg * totalReg - totalReg) / 4;

                arqRand.copiarArquivo(auxRand);
                auxRand.initComp();
                auxRand.initMov();
                inicio = System.currentTimeMillis();
                auxRand.bubbleSort();
                fim = System.currentTimeMillis();
                tempoRand = (fim - inicio) / 1000;
                compRand = auxRand.getComp();
                movRand = auxRand.getMov();
                compERand = (totalReg * totalReg - totalReg) / 2;
                movERand = 3 * (totalReg * totalReg - totalReg) / 4;

                escreverLinha("Bubble Sort",
                                compOrd, compEOrd, movOrd, movEOrd, tempoOrd,
                                compRev, compERev, movRev, movERev, tempoRev,
                                compRand, compERand, movRand, movERand, tempoRand);

                // =================== SHAKE SORT ===================
                System.out.println("SHAKE SORT...");

                arqOrd.copiarArquivo(auxOrd);
                auxOrd.initComp();
                auxOrd.initMov();
                inicio = System.currentTimeMillis();
                auxOrd.shakeSort();
                fim = System.currentTimeMillis();
                tempoOrd = (fim - inicio) / 1000;
                compOrd = auxOrd.getComp();
                movOrd = auxOrd.getMov();
                compEOrd = (totalReg * totalReg - totalReg) / 2;
                movEOrd = 0;

                arqRev.copiarArquivo(auxRev);
                auxRev.initComp();
                auxRev.initMov();
                inicio = System.currentTimeMillis();
                auxRev.shakeSort();
                fim = System.currentTimeMillis();
                tempoRev = (fim - inicio) / 1000;
                compRev = auxRev.getComp();
                movRev = auxRev.getMov();
                compERev = (totalReg * totalReg - totalReg) / 2;
                movERev = 3 * (totalReg * totalReg - totalReg) / 4;

                arqRand.copiarArquivo(auxRand);
                auxRand.initComp();
                auxRand.initMov();
                inicio = System.currentTimeMillis();
                auxRand.shakeSort();
                fim = System.currentTimeMillis();
                tempoRand = (fim - inicio) / 1000;
                compRand = auxRand.getComp();
                movRand = auxRand.getMov();
                compERand = (totalReg * totalReg - totalReg) / 2;
                movERand = 3 * (totalReg * totalReg - totalReg) / 4;

                escreverLinha("Shake Sort",
                                compOrd, compEOrd, movOrd, movEOrd, tempoOrd,
                                compRev, compERev, movRev, movERev, tempoRev,
                                compRand, compERand, movRand, movERand, tempoRand);

                // =================== HEAP SORT ===================
                System.out.println("HEAP SORT...");

                arqOrd.copiarArquivo(auxOrd);
                auxOrd.initComp();
                auxOrd.initMov();
                inicio = System.currentTimeMillis();
                auxOrd.heapSort();
                fim = System.currentTimeMillis();
                tempoOrd = (fim - inicio) / 1000;
                compOrd = auxOrd.getComp();
                movOrd = auxOrd.getMov();
                compEOrd = (int) (totalReg * (Math.log(totalReg) / Math.log(2)));
                movEOrd = compEOrd;

                arqRev.copiarArquivo(auxRev);
                auxRev.initComp();
                auxRev.initMov();
                inicio = System.currentTimeMillis();
                auxRev.heapSort();
                fim = System.currentTimeMillis();
                tempoRev = (fim - inicio) / 1000;
                compRev = auxRev.getComp();
                movRev = auxRev.getMov();
                compERev = (int) (totalReg * (Math.log(totalReg) / Math.log(2)));
                movERev = compERev;

                arqRand.copiarArquivo(auxRand);
                auxRand.initComp();
                auxRand.initMov();
                inicio = System.currentTimeMillis();
                auxRand.heapSort();
                fim = System.currentTimeMillis();
                tempoRand = (fim - inicio) / 1000;
                compRand = auxRand.getComp();
                movRand = auxRand.getMov();
                compERand = (int) (totalReg * (Math.log(totalReg) / Math.log(2)));
                movERand = compERand;

                escreverLinha("Heap Sort",
                                compOrd, compEOrd, movOrd, movEOrd, tempoOrd,
                                compRev, compERev, movRev, movERev, tempoRev,
                                compRand, compERand, movRand, movERand, tempoRand);

                // =================== SHELL SORT ===================
                System.out.println("SHELL SORT...");

                arqOrd.copiarArquivo(auxOrd);
                auxOrd.initComp();
                auxOrd.initMov();
                inicio = System.currentTimeMillis();
                auxOrd.shellSort();
                fim = System.currentTimeMillis();
                tempoOrd = (fim - inicio) / 1000;

                arqRev.copiarArquivo(auxRev);
                auxRev.initComp();
                auxRev.initMov();
                inicio = System.currentTimeMillis();
                auxRev.shellSort();
                fim = System.currentTimeMillis();
                tempoRev = (fim - inicio) / 1000;

                arqRand.copiarArquivo(auxRand);
                auxRand.initComp();
                auxRand.initMov();
                inicio = System.currentTimeMillis();
                auxRand.shellSort();
                fim = System.currentTimeMillis();
                tempoRand = (fim - inicio) / 1000;

                escreverLinha("Shell Sort",
                                auxOrd.getComp(), 0, auxOrd.getMov(), 0, tempoOrd,
                                auxRev.getComp(), 0, auxRev.getMov(), 0, tempoRev,
                                auxRand.getComp(), 0, auxRand.getMov(), 0, tempoRand);

                // =================== QUICK SORT SEM PIVO ===================
                System.out.println("QUICK SORT SEM PIVO...");

                arqOrd.copiarArquivo(auxOrd);
                auxOrd.initComp();
                auxOrd.initMov();
                inicio = System.currentTimeMillis();
                auxOrd.quickSortSemPivo();
                fim = System.currentTimeMillis();
                tempoOrd = (fim - inicio) / 1000;

                arqRev.copiarArquivo(auxRev);
                auxRev.initComp();
                auxRev.initMov();
                inicio = System.currentTimeMillis();
                auxRev.quickSortSemPivo();
                fim = System.currentTimeMillis();
                tempoRev = (fim - inicio) / 1000;

                arqRand.copiarArquivo(auxRand);
                auxRand.initComp();
                auxRand.initMov();
                inicio = System.currentTimeMillis();
                auxRand.quickSortSemPivo();
                fim = System.currentTimeMillis();
                tempoRand = (fim - inicio) / 1000;

                escreverLinha("Quick Sort sem Pivo",
                                auxOrd.getComp(), 0, auxOrd.getMov(), 0, tempoOrd,
                                auxRev.getComp(), 0, auxRev.getMov(), 0, tempoRev,
                                auxRand.getComp(), 0, auxRand.getMov(), 0, tempoRand);

                // =================== QUICK SORT COM PIVO ===================
                System.out.println("QUICK SORT COM PIVO...");

                arqOrd.copiarArquivo(auxOrd);
                auxOrd.initComp();
                auxOrd.initMov();
                inicio = System.currentTimeMillis();
                auxOrd.quickSortComPivo();
                fim = System.currentTimeMillis();
                tempoOrd = (fim - inicio) / 1000;

                arqRev.copiarArquivo(auxRev);
                auxRev.initComp();
                auxRev.initMov();
                inicio = System.currentTimeMillis();
                auxRev.quickSortComPivo();
                fim = System.currentTimeMillis();
                tempoRev = (fim - inicio) / 1000;

                arqRand.copiarArquivo(auxRand);
                auxRand.initComp();
                auxRand.initMov();
                inicio = System.currentTimeMillis();
                auxRand.quickSortComPivo();
                fim = System.currentTimeMillis();
                tempoRand = (fim - inicio) / 1000;

                escreverLinha("Quick Sort com Pivo",
                                auxOrd.getComp(), 0, auxOrd.getMov(), 0, tempoOrd,
                                auxRev.getComp(), 0, auxRev.getMov(), 0, tempoRev,
                                auxRand.getComp(), 0, auxRand.getMov(), 0, tempoRand);

                // =================== COUNTING SORT ===================
                System.out.println("COUNTING SORT...");

                arqOrd.copiarArquivo(auxOrd);
                auxOrd.initComp();
                auxOrd.initMov();
                inicio = System.currentTimeMillis();
                auxOrd.countingSort();
                fim = System.currentTimeMillis();
                tempoOrd = (fim - inicio) / 1000;

                arqRev.copiarArquivo(auxRev);
                auxRev.initComp();
                auxRev.initMov();
                inicio = System.currentTimeMillis();
                auxRev.countingSort();
                fim = System.currentTimeMillis();
                tempoRev = (fim - inicio) / 1000;

                arqRand.copiarArquivo(auxRand);
                auxRand.initComp();
                auxRand.initMov();
                inicio = System.currentTimeMillis();
                auxRand.countingSort();
                fim = System.currentTimeMillis();
                tempoRand = (fim - inicio) / 1000;

                escreverLinha("Counting Sort",
                                auxOrd.getComp(), 0, auxOrd.getMov(), 0, tempoOrd,
                                auxRev.getComp(), 0, auxRev.getMov(), 0, tempoRev,
                                auxRand.getComp(), 0, auxRand.getMov(), 0, tempoRand);

                // =================== BUCKET SORT ===================
                System.out.println("BUCKET SORT...");

                arqOrd.copiarArquivo(auxOrd);
                auxOrd.initComp();
                auxOrd.initMov();
                inicio = System.currentTimeMillis();
                auxOrd.bucketSort();
                fim = System.currentTimeMillis();
                tempoOrd = (fim - inicio) / 1000;

                arqRev.copiarArquivo(auxRev);
                auxRev.initComp();
                auxRev.initMov();
                inicio = System.currentTimeMillis();
                auxRev.bucketSort();
                fim = System.currentTimeMillis();
                tempoRev = (fim - inicio) / 1000;

                arqRand.copiarArquivo(auxRand);
                auxRand.initComp();
                auxRand.initMov();
                inicio = System.currentTimeMillis();
                auxRand.bucketSort();
                fim = System.currentTimeMillis();
                tempoRand = (fim - inicio) / 1000;

                escreverLinha("Bucket Sort",
                                auxOrd.getComp(), 0, auxOrd.getMov(), 0, tempoOrd,
                                auxRev.getComp(), 0, auxRev.getMov(), 0, tempoRev,
                                auxRand.getComp(), 0, auxRand.getMov(), 0, tempoRand);

                // =================== RADIX SORT ===================
                System.out.println("RADIX SORT...");

                arqOrd.copiarArquivo(auxOrd);
                auxOrd.initComp();
                auxOrd.initMov();
                inicio = System.currentTimeMillis();
                auxOrd.radixSort();
                fim = System.currentTimeMillis();
                tempoOrd = (fim - inicio) / 1000;

                arqRev.copiarArquivo(auxRev);
                auxRev.initComp();
                auxRev.initMov();
                inicio = System.currentTimeMillis();
                auxRev.radixSort();
                fim = System.currentTimeMillis();
                tempoRev = (fim - inicio) / 1000;

                arqRand.copiarArquivo(auxRand);
                auxRand.initComp();
                auxRand.initMov();
                inicio = System.currentTimeMillis();
                auxRand.radixSort();
                fim = System.currentTimeMillis();
                tempoRand = (fim - inicio) / 1000;

                escreverLinha("Radix Sort",
                                auxOrd.getComp(), 0, auxOrd.getMov(), 0, tempoOrd,
                                auxRev.getComp(), 0, auxRev.getMov(), 0, tempoRev,
                                auxRand.getComp(), 0, auxRand.getMov(), 0, tempoRand);

                // =================== COMB SORT ===================
                System.out.println("COMB SORT...");

                arqOrd.copiarArquivo(auxOrd);
                auxOrd.initComp();
                auxOrd.initMov();
                inicio = System.currentTimeMillis();
                auxOrd.combSort();
                fim = System.currentTimeMillis();
                tempoOrd = (fim - inicio) / 1000;

                arqRev.copiarArquivo(auxRev);
                auxRev.initComp();
                auxRev.initMov();
                inicio = System.currentTimeMillis();
                auxRev.combSort();
                fim = System.currentTimeMillis();
                tempoRev = (fim - inicio) / 1000;

                arqRand.copiarArquivo(auxRand);
                auxRand.initComp();
                auxRand.initMov();
                inicio = System.currentTimeMillis();
                auxRand.combSort();
                fim = System.currentTimeMillis();
                tempoRand = (fim - inicio) / 1000;

                escreverLinha("Comb Sort",
                                auxOrd.getComp(), 0, auxOrd.getMov(), 0, tempoOrd,
                                auxRev.getComp(), 0, auxRev.getMov(), 0, tempoRev,
                                auxRand.getComp(), 0, auxRand.getMov(), 0, tempoRand);

                // =================== GNOME SORT ===================
                System.out.println("GNOME SORT...");

                arqOrd.copiarArquivo(auxOrd);
                auxOrd.initComp();
                auxOrd.initMov();
                inicio = System.currentTimeMillis();
                auxOrd.gnomeSort();
                fim = System.currentTimeMillis();
                tempoOrd = (fim - inicio) / 1000;

                arqRev.copiarArquivo(auxRev);
                auxRev.initComp();
                auxRev.initMov();
                inicio = System.currentTimeMillis();
                auxRev.gnomeSort();
                fim = System.currentTimeMillis();
                tempoRev = (fim - inicio) / 1000;

                arqRand.copiarArquivo(auxRand);
                auxRand.initComp();
                auxRand.initMov();
                inicio = System.currentTimeMillis();
                auxRand.gnomeSort();
                fim = System.currentTimeMillis();
                tempoRand = (fim - inicio) / 1000;

                escreverLinha("Gnome Sort",
                                auxOrd.getComp(), 0, auxOrd.getMov(), 0, tempoOrd,
                                auxRev.getComp(), 0, auxRev.getMov(), 0, tempoRev,
                                auxRand.getComp(), 0, auxRand.getMov(), 0, tempoRand);

                // =================== TIM SORT ===================
                System.out.println("TIM SORT...");

                arqOrd.copiarArquivo(auxOrd);
                auxOrd.initComp();
                auxOrd.initMov();
                inicio = System.currentTimeMillis();
                auxOrd.timSort();
                fim = System.currentTimeMillis();
                tempoOrd = (fim - inicio) / 1000;

                arqRev.copiarArquivo(auxRev);
                auxRev.initComp();
                auxRev.initMov();
                inicio = System.currentTimeMillis();
                auxRev.timSort();
                fim = System.currentTimeMillis();
                tempoRev = (fim - inicio) / 1000;

                arqRand.copiarArquivo(auxRand);
                auxRand.initComp();
                auxRand.initMov();
                inicio = System.currentTimeMillis();
                auxRand.timSort();
                fim = System.currentTimeMillis();
                tempoRand = (fim - inicio) / 1000;

                escreverLinha("Tim Sort",
                                auxOrd.getComp(), 0, auxOrd.getMov(), 0, tempoOrd,
                                auxRev.getComp(), 0, auxRev.getMov(), 0, tempoRev,
                                auxRand.getComp(), 0, auxRand.getMov(), 0, tempoRand);
        }

        // ----------------------------------------------------------------------

        public void gravaStringNoArquivo(String frase) {
                try {
                        arquivo.writeBytes(frase);
                } catch (IOException ignored) {
                }
        }

        public void escreverCabecalho() {

                String cabecalho = """
                                +--------------------------+------------------------------------------------------+------------------------------------------------------+------------------------------------------------------+
                                |     Métodos Ordenacao    |                   Arquivo Ordenado                   |                Arquivo em Ordem Reversa              |                    Arquivo Randomico                 |
                                +--------------------------|----------|----------|----------|----------|----------+----------|----------|----------|----------|----------+----------|----------|----------|----------|----------|
                                |                          |   Comp.  |   Comp.  |   mov.   |   mov.   |  Tempo   |   Comp.  |   Comp.  |   mov.   |   mov.   |  Tempo   |   Comp.  |   Comp.  |   mov.   |   mov.   |  Tempo   |
                                |                          |   Prog*  |   equa#  |   Prog*  |   equa#  |          |   Prog*  |   equa#  |   Prog*  |   equa#  |          |   Prog*  |   equa#  |   Prog*  |   equa#  |          |
                                +--------------------------|----------|----------|----------|----------|----------+----------|----------|----------|----------|----------+----------|----------|----------|----------|----------|
                                """;
                gravaStringNoArquivo(cabecalho);

        }

        public static String centralizarString(String texto, int larguraCol) {
                int totalEspacos, esq, dir;
                String resultado = "";

                if (texto.length() >= larguraCol)
                        return texto.substring(0, larguraCol);

                totalEspacos = larguraCol - texto.length();
                esq = (totalEspacos + 1) / 2;
                dir = totalEspacos - esq;

                for (int i = 0; i < esq; i++) {
                        resultado += " ";
                }

                resultado += texto;

                for (int i = 0; i < dir; i++) {
                        resultado += " ";
                }

                return resultado;
        }

        public void executarTestes() throws IOException {
                System.out.println("###### TESTE ######");
                arqRand.gerarArquivoRandomico(totalReg);

                String[] ordenacoes = {
                                "Inserção Direta", "Inserção Binária", "Seleção Direta",
                                "Bubble Sort", "Shake Sort", "Heap Sort", "Shell Sort",
                                "Quick Sort sem Pivo", "Quick Sort com Pivo", "Counting Sort",
                                "Bucket Sort", "Radix Sort", "Comb Sort", "Gnome Sort", "Tim Sort"
                };

                for (String ordenacao : ordenacoes) {
                        teste(totalReg, arqRand, auxRand, ordenacao);
                }
        }

        public void teste(int tam, Arquivo arq, Arquivo arqAux, String ordenacao)
                        throws IOException {

                arqAux.truncate(0);
                arq.copiarArquivo(arqAux);

                System.out.println("___________________________________");
                System.out.println(">>>>>>>> Testando '" + ordenacao + "'");

                System.out.print("Desordenado:    ");
                arqAux.exibir();

                switch (ordenacao) {
                        case "Inserção Direta" -> arqAux.insercaoDireta();
                        case "Inserção Binária" -> arqAux.insercaoBinaria();
                        case "Seleção Direta" -> arqAux.selecaoDireta();
                        case "Bubble Sort" -> arqAux.bubbleSort();
                        case "Shake Sort" -> arqAux.shakeSort();
                        case "Heap Sort" -> arqAux.heapSort();
                        case "Shell Sort" -> arqAux.shellSort();
                        case "Quick Sort sem Pivo" -> arqAux.quickSortSemPivo();
                        case "Quick Sort com Pivo" -> arqAux.quickSortComPivo();
                        case "Counting Sort" -> arqAux.countingSort();
                        case "Bucket Sort" -> arqAux.bucketSort();
                        case "Radix Sort" -> arqAux.radixSort();
                        case "Comb Sort" -> arqAux.combSort();
                        case "Gnome Sort" -> arqAux.gnomeSort();
                        case "Tim Sort" -> arqAux.timSort();
                        default -> System.out.println("Algoritmo não reconhecido.");
                }

                System.out.print("Ordenado:    ");
                arqAux.exibir();
        }

        public void closeAll() throws IOException {
                System.out.println("Fechando arqs");
                arqOrd.close();
                arqRev.close();
                arqRand.close();
                auxOrd.close();
                auxRev.close();
                auxRand.close();
        }

}