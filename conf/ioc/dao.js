var ioc = {
	dataSource : {
		type :"org.apache.commons.dbcp.BasicDataSource",
		events : {
			depose :"close"
		},
		fields : {
			driverClassName : 'com.mysql.jdbc.Driver',
			url             : 'jdbc:mysql://61.164.51.238:3306/esup?useUnicode=true&characterEncoding=UTF-8',
			username        : 'esup',
			password        : '221133psw',
			initialSize     : 10,
			maxActive       : 100,
			minIdle         : 10,
			maxIdle         : 20,
			defaultAutoCommit: false,
			
			//validationQueryTimeout : 5,
			validationQuery : "select 1"
		}
	},
    dao : {
        type : "org.nutz.dao.impl.NutDao",
        fields : {
        	dataSource : {refer : 'dataSource'}
        }
    }
};