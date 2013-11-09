package dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TemporalType;

import modelo.PosicaoVeiculo;
import modelo.Veiculo;

@Stateless
public class PosicaoVeiculoDao extends GenericDao<PosicaoVeiculo> {

	@SuppressWarnings("unchecked")
	public List<PosicaoVeiculo> recuperar(
			Veiculo veiculo, Date dataInicial, Date dataFinal) {
		String sql = "select p"
				+ " from PosicaoVeiculo as p" 
				+ " where p.veiculo = :veiculo"
				+ " and p.dataHora between :dataInicial and :dataFinal"
				+ " order by dataHora";

		return (List<PosicaoVeiculo>) getEntityManager().createQuery(sql)
			.setParameter("veiculo", veiculo)
			.setParameter("dataInicial", dataInicial, TemporalType.TIMESTAMP)
			.setParameter("dataFinal", dataFinal, TemporalType.TIMESTAMP)
			.getResultList();
	}
}
