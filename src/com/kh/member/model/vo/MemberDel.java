package com.kh.member.model.vo;

import java.sql.Date;
import java.sql.Timestamp;

// VO class (Value Object)
// DTO class (Data Transfer Object)
// Entity class
// DO class (Domain Object)
// Bean class

// VO객체는 DB 테이블(Entity)의 한 행(record)과 대응한다.
public class MemberDel extends Member {
	private Timestamp delDate;

	public MemberDel() {
		super();
	}

	public MemberDel(String id, String name, String gender, Date birthday, String email, String address,
			Timestamp regDate, Timestamp delDate) {
		super(id, name, gender, birthday, email, address, regDate);
		this.delDate = delDate;
	}

	public Timestamp getDelDate() {
		return this.delDate;
	}

	public void setDelDate(Timestamp delDate) {
		this.delDate = delDate;
	}

	@Override
	public String toString() {
		return super.toString() + "\t" + delDate;
	}

}
