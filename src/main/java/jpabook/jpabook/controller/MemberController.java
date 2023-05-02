package jpabook.jpabook.controller;

import jpabook.jpabook.domain.Address;
import jpabook.jpabook.domain.Member;
import jpabook.jpabook.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult bindingResult) {
        //BindingResult를 안해주면 whitelabel 에러 뜨면서 팅겨버린다.
        //@Valid 한것 다음에 BindingResult가 있으면 오류가 bindingResult에 오류가 담겨서 아래 코드들이 실행이 된다.
        //그리고 다시 members/createMemberForm으로 가서 무슨 에러가 있었는지 뿌려준다. memberForm이름으로 모델을 넘겨준다.
        if (bindingResult.hasErrors()) {
            return "members/createMemberForm";
        }

        //@Valid라고 적어주면 MemberForm에서 자바 검증을 쓰는구나 하고 인식해서. 스프링이 validation 해준다.
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        //엔티티를 그대로 넘겨서 출력하는 것보단 사실 화면에 맞는 Data Transaction Object-DTO로 변환해서 뿌리는게 좋다.
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
