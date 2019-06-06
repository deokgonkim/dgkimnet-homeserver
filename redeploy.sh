echo Enter Server Name; read server_name
echo Enter LDAP URL; read ldap_url
mvn \
-Dmaven.test.skip=true \
-Dtomcat.server.id=${server_name} \
-Dtomcat.manager.url=https://${server_name}/manager/text \
-Dldap.url=${ldap_url} \
-e \
tomcat7:redeploy
