package com.kh.member.controller;

import java.util.List;

import com.kh.member.model.dao.MemberDao;
import com.kh.member.model.service.MemberService;
import com.kh.member.model.vo.Member;

public class MemberController {

	private MemberService memberService = new MemberService();

	public int insertMember(Member member) {
		// 1. service단 업무 요청
		int result = memberService.insertMember(member);
		// 2. view단 처리결과 전달
		return result;
	}

	public List<Member> selectMemberByName(String name) {
		return memberService.selectMemberByName(name);
	}

	public List<? extends Member> selectAllMember(boolean isPresent) {
		return memberService.selectAllMember(isPresent);
	}

	public Member selectOneMember(String id) {
		return memberService.selectOneMember(id);
	}

	public int deleteMember(String id) {
		return memberService.deleteMember(id);
	}

	public int updateMember(Member member, String field, String newValue) {
		return memberService.updateMember(member, field, newValue);
	}
}
