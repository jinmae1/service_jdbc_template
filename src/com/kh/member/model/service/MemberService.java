package com.kh.member.model.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.kh.common.JdbcTemplate;
import com.kh.member.model.dao.MemberDao;
import com.kh.member.model.vo.Member;

import static com.kh.common.JdbcTemplate.getConnection;
import static com.kh.common.JdbcTemplate.commit;
import static com.kh.common.JdbcTemplate.rollback;
import static com.kh.common.JdbcTemplate.close;

// Service Class
// 업무로직 담당(지난 번엔 Dao가 다 했다.)
//
// 1. driver class 등록
// 2. Connection객체 생성(auto commit false처리)
// 3. Dao 요청(Connection객체 전달)

//	/ ====== DAO 담당
// 	/ 3-1. PreparedStatement객체 생성(미완성 쿼리 & 값 대입)
// 	/ 3-2. 쿼리 실행(DML - int, DQL - resultSet) & VO객체 변환
//  / 3-3. 자원 반납(PreparedStatement, resultSet) 여기서 사용한 것만
// 	/ ======

// 4. 트랜잭션 처리(commit/rollback)
// 5. 자원 반납

public class MemberService {

	private MemberDao memberDao = new MemberDao();

	private String driverClass = "oracle.jdbc.OracleDriver";
	private String url = "jdbc:oracle:thin:@192.168.0.2:1521:xe";
	private String user = "student";
	private String password = "student";

	public int insertMember(Member member) {
		Connection conn = getConnection();
		int result = memberDao.insertMember(conn, member);
		if (result > 0)
			commit(conn);
		else
			rollback(conn);
		close(conn);
		return result;
	}

	public int _insertMember(Member member) {
		Connection conn = null;
		int result = 0;
		try {
			// 1.
			Class.forName(driverClass);

			// 2.
			conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false);

			// 3.
			result = memberDao.insertMember(conn, member);

			// 4.
			if (result > 0)
				conn.commit();
			else
				conn.rollback();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public List<Member> selectMemberByName(String name) {
		// 1.
		Connection conn = getConnection();
		// 2.
		List<Member> list = memberDao.selectMemberByName(conn, name);
		// 3.
		close(conn);
		return list;
	}

	public List<? extends Member> selectAllMember(boolean isPresent) {
		Connection conn = getConnection();
		List<? extends Member> list = memberDao.selectAllMember(conn, isPresent);
		close(conn);
		return list;
	}

	public Member selectOneMember(String id) {
		Connection conn = getConnection();
		Member member = memberDao.selectOneMember(conn, id);
		close(conn);

		return member;
	}

	public int deleteMember(String id) {
		Connection conn = getConnection();
		int result = memberDao.deleteMember(conn, id);
		close(conn);
		return result;
	}

	public int updateMember(Member member, String field, String newValue) {
		Connection conn = getConnection();
		int result = memberDao.updateMember(conn, member, field, newValue);
		close(conn);
		return result;
	}

}
