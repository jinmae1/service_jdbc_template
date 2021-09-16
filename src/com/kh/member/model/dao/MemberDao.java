package com.kh.member.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kh.member.model.vo.Member;
import com.kh.member.model.vo.MemberDel;

import static com.kh.common.JdbcTemplate.close;

//	/ ====== DAO 담당
// 	/ 3-1. PreparedStatement객체 생성(미완성 쿼리 & 값 대입)
// 	/ 3-2. 쿼리 실행(DML - int, DQL - resultSet) & VO객체 변환
//  / 3-3. 자원 반납(PreparedStatement, resultSet) 여기서 사용한 것만
// 	/ ======
public class MemberDao {

	public int insertMember(Connection conn, Member member) {
		String sql = "insert into member values(?, ?, ?, ?, ?, ?, default)";
		PreparedStatement pstmt = null;
		int result = 0;
		try {
			// 1.
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getName());
			pstmt.setString(3, member.getGender());
			pstmt.setDate(4, member.getBirthday());
			pstmt.setString(5, member.getEmail());
			pstmt.setString(6, member.getAddress());

			// 2.
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}

		return result;
	}

	public List<Member> selectMemberByName(Connection conn, String name) {
		String sql = "select * from member where name like ?";
		List<Member> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rset = null;

		// 1. PreparedStatement객체 생성 & 값 대입
		// 2. 쿼리 실행
		// 3. ResultSet 처리 -> List<Member>
		// 4. 자원 반납

		try {
			// 1.
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, '%' + name + '%');

			// 2.
			rset = pstmt.executeQuery();

			// 3.
			while (rset.next()) {
				Member member = new Member();
				member.setId(rset.getString("id")); // 컬럼 인덱스로 접근 가능
				member.setName(rset.getString("name"));
				member.setGender(rset.getString("gender"));
				member.setBirthday(rset.getDate("birthday"));
				member.setEmail(rset.getString("email"));
				member.setAddress(rset.getString("address"));
				member.setRegDate(rset.getTimestamp("reg_date"));
				list.add(member);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 4.
			close(rset);
			close(pstmt);
		}

		return list;
	}

	public List<? extends Member> selectAllMember(Connection conn, boolean isPresent) {
		String sql = "select * from member order by reg_date";
		String sql_del = "select * from member_del order by del_date";
		List<Member> list = new ArrayList<>();
		ResultSet rset = null;
		PreparedStatement pstmt = null;

		try {
			if (isPresent) {

				pstmt = conn.prepareStatement(sql);

				rset = pstmt.executeQuery();

				while (rset.next()) {
					Member member = new Member();
					member.setId(rset.getString("id")); // 컬럼 인덱스로 접근 가능
					member.setName(rset.getString("name"));
					member.setGender(rset.getString("gender"));
					member.setBirthday(rset.getDate("birthday"));
					member.setEmail(rset.getString("email"));
					member.setAddress(rset.getString("address"));
					member.setRegDate(rset.getTimestamp("reg_date"));
					list.add(member);
				}
			} else {
				pstmt = conn.prepareStatement(sql_del);

				rset = pstmt.executeQuery();

				while (rset.next()) {
					MemberDel member = new MemberDel();
					member.setId(rset.getString("id")); // 컬럼 인덱스로 접근 가능
					member.setName(rset.getString("name"));
					member.setGender(rset.getString("gender"));
					member.setBirthday(rset.getDate("birthday"));
					member.setEmail(rset.getString("email"));
					member.setAddress(rset.getString("address"));
					member.setRegDate(rset.getTimestamp("reg_date"));
					member.setDelDate(rset.getTimestamp("del_date"));
					list.add(member);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}

		return list;
	}

	public Member selectOneMember(Connection conn, String id) {
		String sql = "select * from member where id = ?";
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Member member = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);

			rset = pstmt.executeQuery();

			if (rset.next()) {
				member = new Member();
				member.setId(rset.getString("id")); // 컬럼 인덱스로 접근 가능
				member.setName(rset.getString("name"));
				member.setGender(rset.getString("gender"));
				member.setBirthday(rset.getDate("birthday"));
				member.setEmail(rset.getString("email"));
				member.setAddress(rset.getString("address"));
				member.setRegDate(rset.getTimestamp("reg_date"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
			close(conn);
		}

		return member;
	}

	public int deleteMember(Connection conn, String id) {
		String sql = "delete from member where id = ?";
		PreparedStatement pstmt = null;
		int result = 0;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);

			result = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
			close(conn);
		}
		return result;
	}

	public int updateMember(Connection conn, Member member, String field, String newValue) {
		PreparedStatement pstmt = null;
		String sql = "update member set " + field + " = ? where id = ?";
		int result = 0;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, newValue);
			pstmt.setString(2, member.getId());

			result = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
			close(conn);
		}

		return result;
	}
}