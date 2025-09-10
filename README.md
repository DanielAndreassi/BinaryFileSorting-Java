# OrdenacaoArquivoBinario

Implementação de algoritmos de ordenação em arquivos binários com análise de desempenho e geração de relatórios comparativos.

## Objetivo

Implementar todos os algoritmos de ordenação especificados para trabalhar com arquivos binários, medindo comparações, movimentações e tempo de execução para três tipos de dados:
- Arquivo ordenado
- Arquivo em ordem reversa  
- Arquivo randômico

## Algoritmos Implementados

### Algoritmos Estudados
- **Inserção Direta** e **Inserção Binária**
- **Seleção Direta**
- **Bolha** e **Shake**
- **Shell**
- **Heap**
- **Quick** (com e sem pivô)
- **Merge** (duas implementações)

### Algoritmos Pesquisados
- **Counting**
- **Bucket**
- **Radix**
- **Comb**
- **Gnome**
- **Tim**

## Especificações

### Arquivos de Teste
- Cada arquivo deve conter pelo menos **1024 registros**
- Três tipos de organização:
  - Arquivo ordenado
  - Arquivo em ordem reversa
  - Arquivo randômico

### Métricas Coletadas
- **Comp. Prog.**: Quantidade de comparações realizadas no algoritmo (apenas envolvendo o campo "Numero")
- **Comp. Equa.**: Valor resultante das equações de complexidade teórica
- **Mov. Prog.**: Quantidade de movimentações no algoritmo (permutação = 2 movimentações)
- **Mov. Equa.**: Valor resultante das equações de complexidade teórica
- **Tempo**: Tempo de execução do algoritmo

### Tabela de Resultados

A seguinte tabela será gerada e gravada em arquivo texto:

| Métodos Ordenação | Arquivo Ordenado | | | | | Arquivo em Ordem Reversa | | | | | Arquivo Randômico | | | | |
|-------------------|----------|----------|----------|----------|----------|----------|----------|----------|----------|----------|----------|----------|----------|----------|----------|
| | Comp. Prog. | Comp. Equa. | Mov. Prog. | Mov. Equa. | Tempo | Comp. Prog. | Comp. Equa. | Mov. Prog. | Mov. Equa. | Tempo | Comp. Prog. | Comp. Equa. | Mov. Prog. | Mov. Equa. | Tempo |
| Inserção Direta | | | | | | | | | | | | | | | |
| Inserção Binária | | | | | | | | | | | | | | | |
| Seleção | | | | | | | | | | | | | | | |
| Bolha | | | | | | | | | | | | | | | |
| Shake | | | | | | | | | | | | | | | |
| Shell | | | | | | | | | | | | | | | |
| Heap | | | | | | | | | | | | | | | |
| Quick s/ pivô | | | | | | | | | | | | | | | |
| Quick c/ pivô | | | | | | | | | | | | | | | |
| Merge 1ª Implement | | | | | | | | | | | | | | | |
| Merge 2ª Implement | | | | | | | | | | | | | | | |
| Counting | | | | | | | | | | | | | | | |
| Bucket | | | | | | | | | | | | | | | |
| Radix | | | | | | | | | | | | | | | |
| Comb | | | | | | | | | | | | | | | |
| Gnome | | | | | | | | | | | | | | | |
| Tim | | | | | | | | | | | | | | | |

**Observações:**
- *Comp. Prog.*: Comparações realizadas no programa (apenas envolvendo o campo "Numero")
- *Comp. Equa.*: Valor das equações de complexidade (não disponível para Shell em diante)
- *Mov. Prog.*: Movimentações no programa (permutação = 2 movimentações)
- *Mov. Equa.*: Valor das equações de complexidade (não disponível para Shell em diante).
