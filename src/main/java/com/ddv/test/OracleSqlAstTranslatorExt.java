package com.ddv.test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.dialect.OracleSqlAstTranslator;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.metamodel.mapping.EntityIdentifierMapping;
import org.hibernate.metamodel.mapping.ModelPart;
import org.hibernate.metamodel.mapping.SelectableMapping;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.sql.ast.spi.SqlSelection;
import org.hibernate.sql.ast.tree.Statement;
import org.hibernate.sql.ast.tree.expression.ColumnReference;
import org.hibernate.sql.ast.tree.from.AbstractTableGroup;
import org.hibernate.sql.ast.tree.from.FromClause;
import org.hibernate.sql.ast.tree.from.TableGroup;
import org.hibernate.sql.ast.tree.select.QueryPart;
import org.hibernate.sql.ast.tree.select.QuerySpec;
import org.hibernate.sql.ast.tree.select.SelectClause;
import org.hibernate.sql.ast.tree.select.SelectStatement;
import org.hibernate.sql.ast.tree.select.SortSpecification;
import org.hibernate.sql.exec.spi.JdbcOperation;
import org.hibernate.sql.exec.spi.JdbcOperationQuerySelect;

public class OracleSqlAstTranslatorExt<T extends JdbcOperation> extends OracleSqlAstTranslator<T> {

	public OracleSqlAstTranslatorExt(SessionFactoryImplementor sessionFactory, Statement statement) {
		super(sessionFactory, statement);
	}

	@Override
	protected void prepareLimitOffsetParameters() {
		super.prepareLimitOffsetParameters();
	}
	
	@Override
	protected boolean shouldEmulateFetchClause(QueryPart queryPart) {
		return super.shouldEmulateFetchClause(queryPart);
		//return false;
	}
	
	@Override
	protected JdbcOperationQuerySelect translateSelect(SelectStatement selectStatement) {
		// TODO Auto-generated method stub
		return super.translateSelect(selectStatement);
	}
	
	
	private List<QualifierAndColumnExpression> extractSorts(QuerySpec querySpec) {
		List<QualifierAndColumnExpression> rslt = new ArrayList<QualifierAndColumnExpression>();
		
		List<SortSpecification> sortSpecifications = querySpec.getSortSpecifications();
		for (SortSpecification sortSpecification : sortSpecifications) {
			ColumnReference columnReference = sortSpecification.getSortExpression().getColumnReference();
			
			rslt.add(new QualifierAndColumnExpression(columnReference.getQualifier(), columnReference.getColumnExpression()));
		}
		return rslt;
	}
	
	private List<QualifierAndColumnExpression> extractIdentifiers(QuerySpec querySpec) {
		List<QualifierAndColumnExpression> rslt = new ArrayList<QualifierAndColumnExpression>();
		
		FromClause fromClause = querySpec.getFromClause();
		List<TableGroup> tableGroups = fromClause.getRoots();
		for (TableGroup tableGroup : tableGroups) {
			tableGroup.getPrimaryTableReference().getIdentificationVariable();	//e1_0
			ModelPart modelPart = tableGroup.getExpressionType();
			
			if (modelPart instanceof AbstractEntityPersister) {
				Class entityType = ((AbstractEntityPersister) modelPart).getClassMetadata().getMappedClass();
				EntityIdentifierMapping mapping = ((AbstractEntityPersister)modelPart).getIdentifierMapping();
				if (mapping instanceof SelectableMapping) {
					Field field = null;
					try {
						field = entityType.getDeclaredField(mapping.getAttributeName());
					} catch (Exception ex) {
						
					}
					
					if (field != null) {
						rslt.add(new QualifierAndColumnExpression(tableGroup.getPrimaryTableReference().getIdentificationVariable(), ((SelectableMapping)mapping).getSelectionExpression(), mapping.getAttributeName()));
						System.out.println("ID " + tableGroup.getPrimaryTableReference().getIdentificationVariable() + "." + ((SelectableMapping)mapping).getSelectionExpression());
					}
					
				}
			}
		}
		return rslt;
	}
	
	@Override
	protected List<SortSpecification> getSortSpecificationsRowNumbering(SelectClause selectClause, QueryPart queryPart) {
		SelectClause selectClauseAdjusted = selectClause;
		
		if (selectClause.isDistinct()) {
			if (queryPart instanceof QuerySpec) {
				selectClauseAdjusted = new SelectClause(selectClause.getSqlSelections().size());
				selectClauseAdjusted.makeDistinct(selectClause.isDistinct());
				
				ArrayList<QualifierAndColumnExpression> qualifiers = new ArrayList<QualifierAndColumnExpression>();
				qualifiers.addAll(extractSorts((QuerySpec)queryPart));
				qualifiers.addAll(extractIdentifiers((QuerySpec)queryPart));
				
				List<SqlSelection> selections = selectClause.getSqlSelections();
				for (SqlSelection selection : selections) {
					ColumnReference columnReference = selection.getExpression().getColumnReference();
					if (qualifiers.contains(new QualifierAndColumnExpression(columnReference.getQualifier(), columnReference.getColumnExpression()))) {
						System.out.println("COLUMN " + columnReference.getQualifier() + "." + columnReference.getColumnExpression());
						selectClauseAdjusted.addSqlSelection(selection);
					}
				}
			}
		}
		
		return super.getSortSpecificationsRowNumbering(selectClauseAdjusted, queryPart);
	}
	
	public static class QualifierAndColumnExpression {
		public String qualifier;
		public String columnName;
		public String fieldName;

		public QualifierAndColumnExpression(String qualifier, String columnName) {
			this(qualifier, columnName, null);
		}
		
		public QualifierAndColumnExpression(String qualifier, String columnName, String fieldName) {
			this.qualifier = qualifier;
			this.columnName = columnName;
			this.fieldName = fieldName;
		}
		
		public boolean equals(Object other) {
			if ((other != null) && (other instanceof QualifierAndColumnExpression)) {
				return this.qualifier.equals(((QualifierAndColumnExpression)other).qualifier)
						&& this.columnName.equals(((QualifierAndColumnExpression)other).columnName);
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return qualifier.hashCode() + columnName.hashCode();
		}
	}
}
