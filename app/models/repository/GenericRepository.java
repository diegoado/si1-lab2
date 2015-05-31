package models.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import play.db.jpa.JPA;

/**
 * Camada genérica para acesso ao Banco de Dados
 */
public abstract class GenericRepository<T> {
	
	// Resultados por página
	public static final int DEFAULT_RESULTS = 50;

	private Class<T> entity;

	public GenericRepository(Class<T> entity) {
		this.entity = entity;
	}

	/**
	 * Persiste uma entidade no Banco de Dados.
	 */
	public void persist(T entity) {
		getEm().persist(entity);
	}

	/**
	 * Espelha o estado do DAO com o banco de Dados, deve ser feito após um
	 * persist, ou merge.
	 */
	public void flush() {
		getEm().flush();
	}

	/**
	 * Atualiza a informação da entidade do código com a do banco de dados.
	 */
	public void merge(T entity) {
		getEm().merge(entity);
	}

	/**
	 * Procura uma certa {@code class} pelo seu {@code id}.
	 */
	public T findByEntityId(Long id) {
		return getEm().find(entity, id);
	}

	/**
	 * Procura todos os objetos de uma certa classe pelo seu {@code className}
	 * descrito na Entidade.
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		String hql = "FROM " + entity.getName();
		Query query = getEm().createQuery(hql);
		
		return query.getResultList();
	}

	/**
	 * Deleta do banco de dados uma {@code classe} referenciada pelo seu
	 * {@code id}.
	 */
	public void removeById(Long id) {
		getEm().remove(findByEntityId(id));
	}

	/**
	 * Remove o respectivo {@code objeto} do banco de dados.
	 */
	public void remove(T entity) {
		getEm().remove(entity);
	}

	/**
	 * Procura uma certa {@code className} pelo seu {@code attributeName}.
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByAttributeName(String attributeName, String attributeValue) {
		String hql = "FROM " + entity.getName() + " c" + " WHERE c."
				   + attributeName + " = '" + attributeValue + "'";
		Query query = getEm().createQuery(hql);
		
		return query.getResultList();
	}

	/**
	 * Cria uma Query HQL
	 */
	public Query createQuery(String query) {
		return getEm().createQuery(query);
	}

	/**
	 * Retorna quantas entidades da {@code class} estão no banco de dados
	 */
	public Long countAll() {
		// Total de entidades
		CriteriaBuilder builder = getEm().getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		query.select(builder.count(query.from(entity)));
		
		return getEm().createQuery(query).getSingleResult();
	}

	/**
	 * Retorna todos as entidades da {@code class} paginado pelo
	 * {@code pageNumber} com o tamanho da {@code pageSize}
	 */
	public List<T> findAll(int pageNumber, int pageSize) {
		CriteriaBuilder criteriaBuilder = getEm().getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entity);
		
		Root<T> from = criteriaQuery.from(entity);
		CriteriaQuery<T> select = criteriaQuery.select(from);
		TypedQuery<T> typedQuery = getEm().createQuery(select);
		typedQuery.setFirstResult((pageNumber - 1) * pageSize);
		typedQuery.setMaxResults(pageSize);
		
		return typedQuery.getResultList();
	}

	public EntityManager getEm() {
		return JPA.em();
	}
}
