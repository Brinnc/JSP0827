package org.sp.springmvc0826.controller;

import java.util.List;

import org.sp.springmvc0826.domain.Board;
import org.sp.springmvc0826.model.BoardDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

//게시판과 관련된 요청을 처리하는 하위 컨트롤러
//@ 란? -> 어노테이션 : 프로그램에서 사용되는 일종의 주석 jdk 5
//스프링의 버전이 올라가면서, xml 설정파일의 내용을 간소화 시키려는 경향이 강해짐
@Controller
public class BoardController {
	//스프링에서의 컨트롤러는 개발자가 메서드명을 자유롭게 정의함
	
	//setter 주입 이외에도, 자동으로 주입받는 방법
	@Autowired
	private BoardDAO boardDAO;
	
	/*
	//BoardDAO의 생성은, 개발자가 직접 new하지 않고 스프링 컨테이너로부터 주입받아야함
	public void setBoardDAO(BoardDAO boardDAO) {
		this.boardDAO = boardDAO;
	}
	*/
	
	//목록 요청을 처리하는 컨트롤러
	@RequestMapping("/board/list")
	public ModelAndView getList() {
		//3단계) DAO에게 일 시키기
		List boardList=boardDAO.selectAll();
		
		//4단계) 결과 저장, 단 request 객체를 직접 이용하지 말고
		//Model은 데이터를 담는 역할, View는 어떤 jsp를 보여줘야할지를 담고 있는 객체
		ModelAndView mav=new ModelAndView();
		mav.addObject("boardList", boardList);
		
		mav.setViewName("board/list");
		
		//System.out.println("목록을 요청함");
		return mav;
	}
	
	//게시판 등록 폼 요청 처리 
	@RequestMapping(value="/board/registform", method=RequestMethod.GET)
	public ModelAndView getRegistForm() {
		//3단계) 생략, 4단계) 저장필요X
		
		ModelAndView mav=new ModelAndView();
		mav.setViewName("board/registform"); //View객체에 jsp접두어 및 접미어 뺀 나머지 부분 대입
		
		return mav;
	}
	
	//게시물 등록 요청 처리
	@RequestMapping(value="/board/regist", method=RequestMethod.POST)
	public ModelAndView regist(Board board) {
		//3단계) 글쓰기
		boardDAO.insert(board);
		//4단계) 결과 저장할 필요 X
		
		ModelAndView mav=new ModelAndView();
		//개발자가 아무것도 명시하지 않으면 default 포워딩임
		//따라서 글쓴 후 list에 재접속하도록 함
		mav.setViewName("redirect:/board/list");
		
		return mav;
	}
	
	@RequestMapping(value="/board/content", method=RequestMethod.GET)
	public ModelAndView getBoard(int board_idx) {
		//3단계) DB에서 레코드 1건 가져오기
		Board board=boardDAO.select(board_idx);
		//4단계) 가져온 것이 있으므로 저장할 필요O
		ModelAndView mav=new ModelAndView();
		mav.addObject("board", board); //모델에 데이터 저장
		mav.setViewName("board/content"); //view에 뷰 이름을 저장
		
		return mav;
	}
	
	@RequestMapping(value="/board/edit", method=RequestMethod.POST)
	public ModelAndView update(Board board) {
		//3단계) 게시물 수정
		boardDAO.update(board);
		
		//4단계) 저장 생략, 상세보기 요청
		ModelAndView mav=new ModelAndView();
		mav.setViewName("redirect:/board/content?board_idx="+board.getBoard_idx());
		
		return mav;
	}
	
	@RequestMapping(value="/board/del", method=RequestMethod.GET)
	public ModelAndView delete(int board_idx) {
		//3단계) 게시물 삭제
		boardDAO.delete(board_idx);
		//4단계) 저장 생략
		ModelAndView mav=new ModelAndView();
		mav.setViewName("redirect:/board/list");
		
		return mav;
	}
	
}
