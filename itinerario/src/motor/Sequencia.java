package motor;

import java.util.List;

public abstract class Sequencia<T> {
	private List<T> itens;
	
	public List<T> getItens() {
		return itens;
	}
	
	public void adicionarItem(T item) {
		itens.add(item);
	}
	
}
