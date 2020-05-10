package cn.Bookspf.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface LoginMapper {
	
	@Select("select uid form login where username=#{username}")
	public Integer findUid(String username);
	
	@Select("select password form login where username=#{username}")
	public String findPassword(String username);
}
