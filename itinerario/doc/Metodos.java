
public void analisarViagens(Date data) {
}

public AnaliseViagem recuperarAnaliseViagem(ProgramacaoRota programacao, Date data) {
}

public AnalisePosicao recuperarAnalisePosicao(PosicaoVeiculo posicao) {
}

public AnaliseParada recuperarAnaliseParada(PontoRota pontoParada) {
}

public List<AnaliseViagem> recuperarAnalisesViagem(Date dataInicial, Date dataFinal) {
}

public List<AnaliseViagem> recuperarAnalisesViagem(Date dataInicial, Date dataFinal, Veiculo veiculo) {
}

public List<AnaliseViagem> recuperarAnalisesViagem(Rota rota, Date dataInicial, Date dataFinal) {
}

public List<AnaliseViagem> recuperarAnalisesViagem(Rota rota, Date dataInicial, Date dataFinal, Veiculo veiculo) {
}

public FechamentoRota recuperarFechamentoRota(Rota rota, Date dataInicial, Date dataFinal) {
}


- em AnaliseViagem 
	acrescentar o atributo FechamentoRota;
	acrescentar Boolean ok. Quando true, não pode reprocessar a análise.
