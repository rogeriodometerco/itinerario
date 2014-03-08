package facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import modelo.Motorista;
import modelo.MotoristaVeiculo;
import modelo.Veiculo;
import dao.MotoristaVeiculoDao;

@Stateless
public class MotoristaVeiculoFacade extends GenericCrudFacade<MotoristaVeiculo> {

	@EJB
	private MotoristaVeiculoDao motoristaVeiculoDao;

	@Override
	protected MotoristaVeiculoDao getDao() {
		return motoristaVeiculoDao;
	}

	public MotoristaVeiculo getMotorista(Veiculo veiculo, Date data) throws Exception {
		MotoristaVeiculo entidade = null;
		List<MotoristaVeiculo> lista = listar(veiculo, data, data);
		if (lista.size() > 0) {
			entidade = lista.get(0);
		}
		return entidade;
	}
	
	public MotoristaVeiculo getVeiculo(Motorista motorista, Date data) throws Exception {
		MotoristaVeiculo entidade = null;
		List<MotoristaVeiculo> lista = listar(motorista, data, data);
		if (lista.size() > 0) {
			entidade = lista.get(0);
		}
		return entidade;
	}
	
	public List<MotoristaVeiculo> listar(Motorista motorista) 
			throws Exception {
		String sql = "select x"
				+ " from MotoristaVeiculo as x" 
				+ " where x.motorista = :motorista";
		return getEntityManager()
				.createQuery(sql, MotoristaVeiculo.class)
				.setParameter("motorista", motorista)
				.getResultList();
	}

	public List<MotoristaVeiculo> listar(Motorista motorista, Date dataInicial, Date dataFinal) 
			throws Exception {
		String sql = "select x"
				+ " from MotoristaVeiculo as x" 
				+ " where x.motorista = :motorista"
				+ " and x.dataInicial <= :dataFinal"
				+ " and (x.dataFinal >= :dataInicial or x.dataFinal is null)";
		return getEntityManager()
				.createQuery(sql, MotoristaVeiculo.class)
				.setParameter("motorista", motorista)
				.setParameter("dataInicial", dataInicial)
				.setParameter("dataFinal", dataFinal)
				.getResultList();
	}

	public List<MotoristaVeiculo> listar(Veiculo veiculo) 
			throws Exception {
		String sql = "select x"
				+ " from MotoristaVeiculo as x" 
				+ " where x.veiculo = :veiculo";
		return getEntityManager()
				.createQuery(sql, MotoristaVeiculo.class)
				.setParameter("veiculo", veiculo)
				.getResultList();
	}

	public List<MotoristaVeiculo> listar(Veiculo veiculo, Date dataInicial, Date dataFinal) 
			throws Exception {
		String sql = "select x"
				+ " from MotoristaVeiculo as x" 
				+ " where x.veiculo = :veiculo"
				+ " and x.dataInicial <= :dataFinal"
				+ " and (x.dataFinal >= :dataInicial or x.dataFinal is null)";
		return getEntityManager()
				.createQuery(sql, MotoristaVeiculo.class)
				.setParameter("veiculo", veiculo)
				.setParameter("dataInicial", dataInicial)
				.setParameter("dataFinal", dataFinal)
				.getResultList();
	}

	public List<MotoristaVeiculo> listar(Motorista motorista, Veiculo veiculo) 
			throws Exception {
		String sql = "select x"
				+ " from MotoristaVeiculo as x" 
				+ " where x.motorista = :motorista"
				+ " and x.veiculo = :veiculo";
		return getEntityManager()
				.createQuery(sql, MotoristaVeiculo.class)
				.setParameter("motorista", motorista)
				.setParameter("veiculo", veiculo)
				.getResultList();
	}

	public List<MotoristaVeiculo> listar(Motorista motorista, Veiculo veiculo, 
			Date dataInicial, Date dataFinal) throws Exception {
		String sql = "select x"
				+ " from MotoristaVeiculo as x" 
				+ " where x.veiculo = :veiculo"
				+ " and x.motorista = :motorista"
				+ " and x.dataInicial <= :dataFinal"
				+ " and (x.dataFinal >= :dataInicial or x.dataFinal is null)";
		return getEntityManager()
				.createQuery(sql, MotoristaVeiculo.class)
				.setParameter("veiculo", veiculo)
				.setParameter("motorista", motorista)
				.setParameter("dataInicial", dataInicial)
				.setParameter("dataFinal", dataFinal)
				.getResultList();
	}

	@Override
	protected void validar(MotoristaVeiculo entidade) throws Exception {
		List<String> erros = new ArrayList<String>();
		if (entidade.getMotorista() == null) {
			erros.add("Informe o motorista");
		}
		if (entidade.getVeiculo() == null) {
			erros.add("Informe o veiculo");
		}
		if (entidade.getDataInicial() == null) {
			erros.add("Informe a data inicial");
		}
		if (entidade.getDataFinal() != null && entidade.getDataInicial().after(entidade.getDataFinal())) {
			erros.add("Data inicial não pode ser maior que a data final");
		}
		if (entidade.getId() != null) {
			MotoristaVeiculo m = recuperar(entidade.getId());
			if (m.getId().equals(entidade.getId())) {
				if (entidade.getMotorista() != null && m.getMotorista() != null
						&& !m.getMotorista().getId().equals(entidade.getMotorista().getId())) {
					erros.add("Motorista não pode ser alterado, exclua o registro ou cadastre um novo");
				}
				if (entidade.getVeiculo() != null && m.getVeiculo() != null
						&& !m.getVeiculo().getId().equals(entidade.getVeiculo().getId())) {
					erros.add("Veículo não pode ser alterado, exclua o registro ou cadastre um novo");
				}
			}
		}

		// TODO Verificar se possui intersecção de período para mesmo veículo e/ou motorista.
	}

	@Override
	protected void validarExclusao(MotoristaVeiculo entidade) throws Exception {
		List<String> erros = new ArrayList<String>();

		// TODO Ver o que precisa validar.
		if (erros.size() > 0) {
			throw new Exception(erros.toString());
		}
	}


}
