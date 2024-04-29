package com.example.LibraryService.service.impl;

import com.example.LibraryService.entity.Parent;
import com.example.LibraryService.entity.User;
import com.example.LibraryService.entity.UserEmail;
import com.example.LibraryService.exceptions.BadRequest;
import com.example.LibraryService.payload.RegisterPayload;
import com.example.LibraryService.payload.Result;
import com.example.LibraryService.repository.ParentRepository;
import com.example.LibraryService.repository.RoleRepository;
import com.example.LibraryService.repository.UserEmailRepository;
import com.example.LibraryService.repository.UserRepository;
import com.example.LibraryService.service.UserEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import javax.mail.internet.MimeMessage;
import java.util.Collections;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserEmailServiceImpl implements UserEmailService {
    private final UserEmailRepository userEmailRepository;
    private final ParentRepository parentRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override  // this method saves user to user_email table if user enter email code, this object is moved to parent table
    public Result saveUserByEmailCode(RegisterPayload registerPayload) {
        if (parentRepository.findByUserName(registerPayload.getUserName()) != null)
            return Result.exception(new BadRequest("This email is already registered"));
        else if (parentRepository.findByEmail(registerPayload.getEmail()) != null)
            return Result.exception(new BadRequest("This username is already registered"));

        try {
            // delete user_email if user has tried to register before
            if (userEmailRepository.existsByEmail(registerPayload.getEmail()))
                userEmailRepository.deleteUserEmailByEmail(registerPayload.getEmail());

            UserEmail userEmail = new UserEmail();
            userEmail.setUserName(registerPayload.getUserName());
            userEmail.setPassword(registerPayload.getPassword());
            userEmail.setEmail(registerPayload.getEmail());
            userEmail.setEmailCode(generateEmailCode());
            userEmail.setBio(registerPayload.getBio());
            userEmail.setFirstName(registerPayload.getFirstName());
            userEmail.setLastName(registerPayload.getLastName());

            // save user_email to user_email table
            userEmailRepository.save(userEmail);

            sendCodeToEmail(userEmail.getEmail(), userEmail.getEmailCode());
            return Result.message("user is registered, but please confirm the email of user", true);
        } catch (Exception e) {
            return Result.exception(e);
        }
    }

    @Override // Generating email confirmation code for registration
    public String generateEmailCode() {
        Random rnd = new Random();
        int number = rnd.nextInt(9999);
        return String.format("%04d", number);
    }

    @Override // send code to email (this method requires toEmail - receiver's email, code - confirmation code)
    public void sendCodeToEmail(String toEmail, String code) {

        String builderTemplate="<!DOCTYPE HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
                "<head>\n" +
                "    <!--[if gte mso 9]>\n" +
                "    <xml>\n" +
                "        <o:OfficeDocumentSettings>\n" +
                "            <o:AllowPNG/>\n" +
                "            <o:PixelsPerInch>96</o:PixelsPerInch>\n" +
                "        </o:OfficeDocumentSettings>\n" +
                "    </xml>\n" +
                "    <![endif]-->\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <meta name=\"x-apple-disable-message-reformatting\">\n" +
                "    <!--[if !mso]><!--><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><!--<![endif]-->\n" +
                "    <title></title>\n" +
                "\n" +
                "    <style type=\"text/css\">\n" +
                "      @media only screen and (min-width: 520px) {\n" +
                "  .u-row {\n" +
                "    width: 500px !important;\n" +
                "  }\n" +
                "  .u-row .u-col {\n" +
                "    vertical-align: top;\n" +
                "  }\n" +
                "\n" +
                "  .u-row .u-col-100 {\n" +
                "    width: 500px !important;\n" +
                "  }\n" +
                "\n" +
                "}\n" +
                "\n" +
                "@media (max-width: 520px) {\n" +
                "  .u-row-container {\n" +
                "    max-width: 100% !important;\n" +
                "    padding-left: 0px !important;\n" +
                "    padding-right: 0px !important;\n" +
                "  }\n" +
                "  .u-row .u-col {\n" +
                "    min-width: 320px !important;\n" +
                "    max-width: 100% !important;\n" +
                "    display: block !important;\n" +
                "  }\n" +
                "  .u-row {\n" +
                "    width: 100% !important;\n" +
                "  }\n" +
                "  .u-col {\n" +
                "    width: 100% !important;\n" +
                "  }\n" +
                "  .u-col > div {\n" +
                "    margin: 0 auto;\n" +
                "  }\n" +
                "}\n" +
                "body {\n" +
                "  margin: 0;\n" +
                "  padding: 0;\n" +
                "}\n" +
                "\n" +
                "table,\n" +
                "tr,\n" +
                "td {\n" +
                "  vertical-align: top;\n" +
                "  border-collapse: collapse;\n" +
                "}\n" +
                "\n" +
                "p {\n" +
                "  margin: 0;\n" +
                "}\n" +
                "\n" +
                ".ie-container table,\n" +
                ".mso-container table {\n" +
                "  table-layout: fixed;\n" +
                "}\n" +
                "\n" +
                "* {\n" +
                "  line-height: inherit;\n" +
                "}\n" +
                "\n" +
                "a[x-apple-data-detectors='true'] {\n" +
                "  color: inherit !important;\n" +
                "  text-decoration: none !important;\n" +
                "}\n" +
                "\n" +
                "table, td { color: #000000; } </style>\n" +
                "\n" +
                "\n" +
                "\n" +
                "</head>\n" +
                "\n" +
                "<body class=\"clean-body u_body\" style=\"margin: 0;padding: 0;-webkit-text-size-adjust: 100%;background-color: #e7e7e7;color: #000000\">\n" +
                "<!--[if IE]><div class=\"ie-container\"><![endif]-->\n" +
                "<!--[if mso]><div class=\"mso-container\"><![endif]-->\n" +
                "<table style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 320px;Margin: 0 auto;background-color: #e7e7e7;width:100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "    <tbody>\n" +
                "    <tr style=\"vertical-align: top\">\n" +
                "        <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top\">\n" +
                "            <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align=\"center\" style=\"background-color: #e7e7e7;\"><![endif]-->\n" +
                "\n" +
                "\n" +
                "\n" +
                "            <div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\n" +
                "                <div class=\"u-row\" style=\"margin: 0 auto;min-width: 320px;max-width: 500px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\n" +
                "                    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n" +
                "                        <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:500px;\"><tr style=\"background-color: transparent;\"><![endif]-->\n" +
                "\n" +
                "                        <!--[if (mso)|(IE)]><td align=\"center\" width=\"500\" style=\"width: 500px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->\n" +
                "                        <div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 500px;display: table-cell;vertical-align: top;\">\n" +
                "                            <div style=\"height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\n" +
                "                                <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->\n" +
                "\n" +
                "                                <table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "                                    <tbody>\n" +
                "                                    <tr>\n" +
                "                                        <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "\n" +
                "                                            <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "                                                <tr>\n" +
                "                                                    <td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\">\n" +
                "\n" +
                "                                                        <img align=\"center\" border=\"0\" src=\"image-1.png\" alt=\"\" title=\"\" style=\"outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;clear: both;display: inline-block !important;border: none;height: auto;float: none;width: 100%;max-width: 480px;\" width=\"480\"/>\n" +
                "\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "                                            </table>\n" +
                "\n" +
                "                                        </td>\n" +
                "                                    </tr>\n" +
                "                                    </tbody>\n" +
                "                                </table>\n" +
                "\n" +
                "                                <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                "                            </div>\n" +
                "                        </div>\n" +
                "                        <!--[if (mso)|(IE)]></td><![endif]-->\n" +
                "                        <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "            <div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\n" +
                "                <div class=\"u-row\" style=\"margin: 0 auto;min-width: 320px;max-width: 500px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\n" +
                "                    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n" +
                "                        <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:500px;\"><tr style=\"background-color: transparent;\"><![endif]-->\n" +
                "\n" +
                "                        <!--[if (mso)|(IE)]><td align=\"center\" width=\"500\" style=\"width: 500px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->\n" +
                "                        <div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 500px;display: table-cell;vertical-align: top;\">\n" +
                "                            <div style=\"height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\n" +
                "                                <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->\n" +
                "\n" +
                "                                <table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "                                    <tbody>\n" +
                "                                    <tr>\n" +
                "                                        <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "\n" +
                "                                            <!--[if mso]><table width=\"100%\"><tr><td><![endif]-->\n" +
                "                                            <h1 style=\"margin: 0px; line-height: 140%; text-align: center; word-wrap: break-word; font-size: 22px; font-weight: 400;\"><span>Email confirmation</span></h1>\n" +
                "                                            <!--[if mso]></td></tr></table><![endif]-->\n" +
                "\n" +
                "                                        </td>\n" +
                "                                    </tr>\n" +
                "                                    </tbody>\n" +
                "                                </table>\n" +
                "\n" +
                "                                <table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "                                    <tbody>\n" +
                "                                    <tr>\n" +
                "                                        <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "\n" +
                "                                            <div style=\"font-size: 14px; line-height: 140%; text-align: left; word-wrap: break-word;\">\n" +
                "                                                <p style=\"line-height: 140%;\">Hi, this is your confirm code to active your profile: <br /><strong>"+code+"</strong> </p>\n" +
                "                                                <p style=\"line-height: 140%;\"> </p>\n" +
                "                                                <p style=\"line-height: 140%;\"><em>Good Luck :)</em><br /><br /><strong>Backend Developer: Abror Allaberganov</strong></p>\n" +
                "                                                <p style=\"line-height: 140%;\"><strong>Fronted Developer: Sherzod Qodirov</strong></p>\n" +
                "                                            </div>\n" +
                "\n" +
                "                                        </td>\n" +
                "                                    </tr>\n" +
                "                                    </tbody>\n" +
                "                                </table>\n" +
                "\n" +
                "                                <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                "                            </div>\n" +
                "                        </div>\n" +
                "                        <!--[if (mso)|(IE)]></td><![endif]-->\n" +
                "                        <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "\n" +
                "\n" +
                "\n" +
                "            <!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "    </tbody>\n" +
                "</table>\n" +
                "<!--[if mso]></div><![endif]-->\n" +
                "<!--[if IE]></div><![endif]-->\n" +
                "</body>\n" +
                "\n" +
                "</html>\n";
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(toEmail);
            helper.setSubject("Gmail Confirmation");
            helper.setText(builderTemplate, true); // set true for HTML format
            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new BadRequest(e.getMessage());
        }
    }

    @Override // check the code which is entered by user
    public Result checkEmailCode(String email, String code) {
        try {
            System.out.println(email+" - "+code);
            UserEmail userEmail = userEmailRepository.findUserEmailByEmail(email);

            if (userEmail.getEmailCode().equals(code)) {
                Parent parent = new Parent();
                parent.setEmail(userEmail.getEmail());
                parent.setPassword(passwordEncoder.encode(userEmail.getPassword()));
                parent.setUserName(userEmail.getUserName());

                parent.setRoles(Collections.singletonList(roleRepository.findByName("ROLE_USER")));


                parentRepository.save(parent); // if code is correct, the data of user is relocated to parent table

                User user=new User();
                user.setFirstName(userEmail.getFirstName());
                user.setLastName(userEmail.getLastName());
                user.setBio(userEmail.getBio());
                user.setParent(parent);

                userRepository.save(user);

                if (userEmailRepository.existsByEmail(fromEmail))
                    userEmailRepository.deleteUserEmailByEmail(email); // the user_email object is deleted after saving user data

                return Result.success(user);
            }

            return Result.message("code is incorrect, please try again", true);
        }catch (Exception e){
            return Result.exception(e);
        }
    }


}
