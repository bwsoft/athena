package com.bwsoft.poc.security_check;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomerizedUserDetailService implements UserDetailsService {

	private String dsName;

	public void setDsName(String dsName) {
		this.dsName = dsName;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		DataSource ds; 
		try {
			Context ctx = new InitialContext();
			Context envCtx = (Context) ctx.lookup("java:comp/env");

			ds = (DataSource) envCtx.lookup(dsName);
		} catch (NamingException e1) {
			throw new UsernameNotFoundException("unrecognuzed user or wrong password");
		}		
		
		try ( Connection conn = ds.getConnection() )
		{
			try( PreparedStatement stat = conn.prepareStatement("select password, enabled from users where username=?") ) {
				stat.setString(1, username);
				try ( ResultSet rs = stat.executeQuery() ) {
					if( rs.next() ) {
						String passwd = rs.getString(1);
						boolean enabled = rs.getBoolean(2);
						Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>(); 
						try( PreparedStatement roleStat = conn.prepareStatement("select role from user_roles where username=?") ) {
							roleStat.setString(1, username);
							try( ResultSet rs1 = roleStat.executeQuery() ) {
								while( rs1.next() ) {
									setAuths.add(new SimpleGrantedAuthority(rs1.getString(1)));
								}								
							}
						}
						return new User(username, passwd, enabled, true, true, true, setAuths);
					} else {
						throw new UsernameNotFoundException("unrecognuzed user or wrong password");
					}
				}
			}
		} catch (SQLException e) {
			throw new UsernameNotFoundException("unrecognuzed user or wrong password");
		}		
	}
}
