package com.asklepios.hospitalreservation_asklepios.Controller;

import com.asklepios.hospitalreservation_asklepios.Service.IF_ReservationService;
import com.asklepios.hospitalreservation_asklepios.Service.IF_UserService;
import com.asklepios.hospitalreservation_asklepios.Util.Profile_ImageUtil;
import com.asklepios.hospitalreservation_asklepios.VO.DoctorVO;
import com.asklepios.hospitalreservation_asklepios.VO.ReservationStatusVO;
import com.asklepios.hospitalreservation_asklepios.VO.ReservationVO;
import com.asklepios.hospitalreservation_asklepios.VO.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class MyPageController {

    @Autowired
    IF_UserService userService;

    @Autowired
    IF_ReservationService reservationService;

    @Autowired
    Profile_ImageUtil profileImageUtil;

//    @Value("${upload.file.path}")
//    private String filePath;

    @GetMapping("/myPage")
    public String myPage(@RequestParam("user_id") String user_id, Model model) {
        if (user_id == null) {
            return "redirect:/login";
        } else {
            // UserVO 불러오기
            UserVO userVO = userService.printOneInfo(user_id);
            userVO.divideEngName();
            userVO.divideAddr();
            userVO.divideEmail();
            userVO.divideTel();
            String filePath = "profile_image/" + userVO.getUser_image();

            // DoctorVO 불러오기
            DoctorVO doctorVO = userService.printOneDoctorInfo(user_id);

            // Reservation 불러오기
//            List<ReservationStatusVO> reservationStatusVOList = reservationService.findAllReservation(user_id);
//            List<ReservationStatusVO> reservationDoctorStatusVOList = reservationService.findAllDoctorReservation(user_id);

            model.addAttribute("filePath", filePath);
            model.addAttribute("userVO", userVO);
            model.addAttribute("doctorVO", doctorVO);
//            model.addAttribute("reservationStatusVOList", reservationStatusVOList);
//            model.addAttribute("reservationDoctorStatusVOList", reservationDoctorStatusVOList);
            return "MyPage/myPage";
        }
    }

    @ResponseBody
    @GetMapping("/doctorreservationstatus")
    public List<ReservationStatusVO> doctorReservationStatus(@RequestParam("user_id") String user_id) {
        return reservationService.findAllDoctorReservation(user_id);
    }

    @ResponseBody
    @GetMapping("/userreservationstatus")
    public List<ReservationStatusVO> userReservationStatus(@RequestParam("user_id") String user_id) {
        return reservationService.findAllReservation(user_id);
    }

    @ResponseBody
    @GetMapping("/acceptreservation")
    public void acceptReservation(@RequestParam("reservation_code") String reservation_code) {
        System.out.println(reservation_code);
        reservationService.accept(reservation_code);
    }

    @ResponseBody
    @GetMapping("/cancelreservation")
    public void cancelReservation(@RequestParam("reservation_code") String reservation_code) {
        System.out.println(reservation_code);
        reservationService.cancel(reservation_code);
    }

    @ResponseBody
    @PostMapping("/verify_password_mypage")
    public String verify_password_mypage(@RequestParam("user_id") String user_id) {
        return userService.checkedPassword(user_id);
    }

    @PostMapping(value = "/updateUserInfo")
    public String modifyProfile(@ModelAttribute UserVO userVO, @ModelAttribute DoctorVO doctorVO, @RequestParam(value = "file", required = false) MultipartFile file, Model model) throws Exception {
        String newFileName = profileImageUtil.storeFile(file);
        System.out.println(userVO.toString());
        System.out.println(doctorVO.toString());
        userVO.setUser_image(newFileName);
        String doctor_id = userVO.getUser_id();
        model.addAttribute("user_id", doctor_id);
        doctorVO.setUser_doctor_id(doctor_id);
        userService.modifyUserDoctorInfo(doctorVO);
        userService.modifyUserCommonInfo(userVO);
//        System.out.println(userVO.getUser_authority());
        return "MyPage/myPage";
    }
}