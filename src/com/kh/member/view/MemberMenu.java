package com.kh.member.view;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;

import com.kh.member.controller.MemberController;
import com.kh.member.model.vo.Member;
import com.kh.member.model.vo.MemberDel;

public class MemberMenu {
	private Scanner sc = new Scanner(System.in);
	private MemberController memberController = new MemberController();

	public void mainMenu() {
		String menu = "***** 회원 관리 프로그램 *****\n" + "1. 전체 조회\n" + "2. 아이디 조회\n" + "3. 이름 검색\n" + "4. 회원 가입\n"
				+ "5. 회원 정보 변경\n" + "6. 회원 탈퇴\n" + "7. 탈퇴 회원 조회\n" + "0. 프로그램 종료\n" + "*****************************\n"
				+ "선택: ";

		while (true) {
			System.out.println();
			System.out.print(menu);
			String choice = sc.next();
			List<? extends Member> list = null;
			String id = null;
			String name = null;
			int result = 0;
			Member member = null;

			switch (choice) {
				case "1":
					list = memberController.selectAllMember(true);
					printMemberList(list);
					break;

				case "2":
					id = inputId("조회할 아이디: ");
					member = memberController.selectOneMember(id);
					printMember(member);
					break;

				case "3":
					printMemberList(memberController.selectMemberByName(inputName()));
					break;

				case "4":
					System.out.println(memberController.insertMember(inputMember()) > 0 ? "회원가입 성공" : "회원가입 실패");
					break;

				case "5":
					id = inputId("수정할 아이디: ");
					member = memberController.selectOneMember(id);

					if (!userExists(member)) {
						System.out.println("해당하는 회원이 없습니다.");
						break;
					}
					printMember(member);
					updateMenu(member);
					break;

				case "6":
					id = inputId("삭제할 아이디를 입력하세요: ");
					System.out.println("member@menu = " + id);
					result = memberController.deleteMember(id);
					System.out.println(result > 0 ? "회원 삭제 성공!" : "회원 삭제 실패!");
					break;

				case "7":
					list = memberController.selectAllMember(false);
					printMemberList(list);
					break;

				case "0":
					break;

				default:
					System.out.println("잘못 입력하셨습니다.");
					break;
			}

		}
	}

	private String inputId(String msg) {
		System.out.println(msg);
		return sc.next();
	}

	private String inputName() {
		System.out.println("검색할 이름");
		return sc.next();
	}

	private boolean userExists(String id) {
		Member member = memberController.selectOneMember(id);
		return member == null ? false : true;
	}

	private boolean userExists(Member member) {
		return member == null ? false : true;
	}

	private void printMemberList(List<? extends Member> list) {
		if (list.isEmpty()) {
			System.out.println("조회된 행이 없습니다.");
		} else {
			String header = "id	name		gender		birthday		email		address					reg_date";
			if (list.get(0) instanceof MemberDel)
				header += "		del_date";
			System.out.println(
					"--------------------------------------------------------------------------------------------");
			System.out.println(header);
			System.out.println(
					"--------------------------------------------------------------------------------------------");
			for (Member member : list) {
				System.out.println(member);
			}
			System.out.println(
					"--------------------------------------------------------------------------------------------");

		}
	}

	private void printMember(Member member) {
		if (member == null)
			System.out.println("해당하는 회원이 없습니다.");
		else {
			System.out.println("-----------------------------------------");
			System.out.println("아이디: " + member.getId());
			System.out.println("이름: " + member.getName());
			System.out.println("성별: " + member.getGender());
			System.out.println("이메일: " + member.getEmail());
			System.out.println("주소: " + member.getAddress());
			SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm");
			System.out.println("등록일: " + sdf.format(member.getRegDate()));
			System.out.println("-----------------------------------------");
		}
	}

	private Member inputMember() {
		String id = null;
		boolean usrExists;

		System.out.println("새 회원정보를 입력하세요");
		do {
			id = inputId("아이디: ");
			usrExists = userExists(id);

			if (usrExists)
				System.out.println("아이디가 이미 존재합니다");

		} while (usrExists);

		System.out.print("이름: ");
		String name = sc.next();

		System.out.print("성별(M/F): ");
		String gender = String.valueOf(sc.next().toUpperCase().charAt(0));

		// 사용자 입력값 java.util.Calendar -> java.sql.Date
		System.out.print("생년월일(예: 19900909): ");
		String temp = sc.next();
		int year = Integer.valueOf(temp.substring(0, 4));
		int month = Integer.valueOf(temp.substring(4, 6)) - 1; // 0 ~ 11월
		int day = Integer.valueOf(temp.substring(6, 8));
		Calendar cal = new GregorianCalendar(year, month, day);
		Date birthday = new Date(cal.getTimeInMillis());

		System.out.print("이메일: ");
		String email = sc.next();

		sc.nextLine();
		System.out.print("주소: ");
		String address = sc.nextLine();

		return new Member(id, name, gender, birthday, email, address, null);
	}

	private void updateMenu(Member member) {
		String menu = "------ 회원정보 수정메뉴 ------\n" + "1. 이름 변경\n" + "2. 이메일 변경\n" + "3. 주소 변경\n" + "0. 메인메뉴 돌아가기\n"
				+ "---------------------------\n" + "선택 : ";

		while (true) {
			System.out.print(menu);
			String choice = sc.next();
			int result = 0;
			String newValue = null;

			switch (choice) {
				case "1":
					newValue = updateInfo("이름", member.getName());
					result = memberController.updateMember(member, "name", newValue);
					if (printResult("이름", result))
						member.setName(newValue);
					break;

				case "2":
					newValue = updateInfo("이메일", member.getEmail());
					result = memberController.updateMember(member, "email", newValue);
					if (printResult("이메일", result))
						member.setEmail(newValue);
					break;

				case "3":
					newValue = updateInfo("주소", member.getAddress());
					result = memberController.updateMember(member, "address", newValue);
					if (printResult("주소", result))
						member.setAddress(newValue);
					break;

				case "0":
					return;

				default:
					System.out.println("잘못 입력하셨습니다.");
					break;
			}
		}
	}

	private String updateInfo(String field, String oldValue) {
		System.out.print("변경할 " + field + "을(를) 입력하세요(현재값: " + oldValue + "): ");
		sc.nextLine();
		return sc.nextLine();
	}

	private boolean printResult(String field, int result) {
		if (result > 0) {
			System.out.println(field + " 변경 성공");
			return true;
		} else {
			System.out.println(field + "변경 실패");
			return false;
		}
	}
}
