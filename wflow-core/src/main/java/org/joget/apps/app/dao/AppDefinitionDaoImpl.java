package org.joget.apps.app.dao;

import java.util.Collection;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.joget.apps.app.model.AppDefinition;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 * DAO to load/store AppDefinition objects
 */
public class AppDefinitionDaoImpl extends AbstractVersionedObjectDao<AppDefinition> implements AppDefinitionDao {

    public static final String ENTITY_NAME = "AppDefinition";

    @Override
    public String getEntityName() {
        return ENTITY_NAME;
    }

    @Override
    public void delete(AppDefinition obj) {
        // disassociate
        if (obj != null) {
            if (obj.getDatalistDefinitionList() != null) {
                obj.getDatalistDefinitionList().clear();
            }
            if (obj.getFormDefinitionList() != null) {
                obj.getFormDefinitionList().clear();
            }
            if (obj.getUserviewDefinitionList() != null) {
                obj.getUserviewDefinitionList().clear();
            }
            if (obj.getPackageDefinitionList() != null) {
                obj.getPackageDefinitionList().clear();
            }
            if (obj.getPluginDefaultPropertiesList() != null) {
                obj.getPluginDefaultPropertiesList().clear();
            }
            if (obj.getEnvironmentVariableList() != null) {
                obj.getEnvironmentVariableList().clear();
            }
            if (obj.getMessageList() != null) {
                obj.getMessageList().clear();
            }
            super.saveOrUpdate(obj);
        }

        // delete
        super.delete(obj);
    }

    public Long getPublishedVersion(final String appId) {
        // execute query and return result
        Long result = (Long) this.findHibernateTemplate().execute(
                new HibernateCallback() {

                    public Object doInHibernate(Session session) throws HibernateException {
                        String query = "SELECT version FROM " + getEntityName() + " e  where 1=1 AND e.published = true and appId=?";
                        Query q = session.createQuery(query);

                        q.setParameter(0, appId);

                        return ((Long) q.iterate().next()).longValue();
                    }
                });

        return result;
    }

    public Collection<AppDefinition> findPublishedApps(final String sort, final Boolean desc, final Integer start, final Integer rows) {
        Collection<AppDefinition> resultList = (Collection) this.findHibernateTemplate().execute(
                new HibernateCallback() {

                    public Object doInHibernate(Session session) throws HibernateException {
                        String query = "SELECT e FROM " + getEntityName() + " e WHERE 1=1 AND e.published = true";

                        if (sort != null && !sort.equals("")) {
                            query += " ORDER BY " + sort;

                            if (desc) {
                                query += " DESC";
                            }
                        }
                        Query q = session.createQuery(query);

                        int s = (start == null) ? 0 : start;
                        q.setFirstResult(s);

                        if (rows != null && rows > 0) {
                            q.setMaxResults(rows);
                        }

                        return q.list();
                    }
                });

        return resultList;
    }
}
