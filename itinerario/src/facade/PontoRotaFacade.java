package facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import modelo.PontoRota;
import modelo.Rota;
import dao.PontoRotaDao;

@Stateless
public class PontoRotaFacade extends GenericCrudFacade<PontoRota> {

        @EJB
        private PontoRotaDao dao;

        @SuppressWarnings("unchecked")
        public List<PontoRota> recuperarPontos(Rota rota) throws Exception {
                String sql = "select p"
                                + " from PontoRota as p" 
                                + " where p.rota = :rota"
                                + " order by sequencia";

                return (List<PontoRota>) getEntityManager().createQuery(sql)
                                .setParameter("rota", rota)
                                .getResultList();

        }

        @Override
        protected PontoRotaDao getDao() {
                return dao;
        }

}