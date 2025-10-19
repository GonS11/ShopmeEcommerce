package com.shopme.admin.setting;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Currency;
import com.shopme.common.entity.setting.Setting;

@Controller
public class SettingController
{
    @Autowired
    private SettingService service;
    @Autowired
    private CurrencyRepository currencyRepo;

    @Value("${app.upload.dir:../site-logo/}")
    private String uploadDirectory;

    @GetMapping("/settings")
    public String listAll(Model model)
    {
        List<Setting> listSettings = service.listAllSettings();
        List<Currency> listCurrencies = currencyRepo.findAllByOrderByNameAsc();

        model.addAttribute("listCurrencies", listCurrencies);

        for (Setting setting : listSettings)
        {
            model.addAttribute(setting.getKey(), setting.getValue());
        }

        return "settings/settings";
    }

    @PostMapping("/settings/save_general")
    public String saveGeneralSettings(@RequestParam("fileImage") MultipartFile multipartFile,
                                      HttpServletRequest request,
                                      RedirectAttributes ra)
            throws IOException
    {
        GeneralSettingBag settingBag = service.getGeneralSettings();

        saveSiteLogo(multipartFile, settingBag);
        saveCurrencySymbol(request, settingBag);

        updateSettingValuesFromForm(request, settingBag.list());

        ra.addFlashAttribute("message", "General settings have been saved.");

        return "redirect:/settings";
    }

    private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag settingBag)
            throws IOException
    {
        if (!multipartFile.isEmpty())
        {
            String originalFilename = multipartFile.getOriginalFilename();

            if (originalFilename == null || originalFilename.trim().isEmpty())
            {
                throw new IllegalArgumentException("Invalid file name");
            }

            String fileName = StringUtils.cleanPath(originalFilename);
            String value = "/site-logo/" + fileName;
            settingBag.updateSiteLogo(value);

            FileUploadUtil.cleanDir(uploadDirectory);
            FileUploadUtil.saveFile(uploadDirectory, fileName, multipartFile);
        }
    }

    private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag settingBag)
    {
        String currencyIdParam = request.getParameter("CURRENCY_ID");

        if (currencyIdParam == null || currencyIdParam.trim().isEmpty())
        {
            return;
        }

        try
        {
            Integer currencyId = Integer.parseInt(currencyIdParam);
            Optional<Currency> findByIdResult = currencyRepo.findById(currencyId);

            findByIdResult.ifPresent(currency ->
                                             settingBag.updateCurrencySymbol(currency.getSymbol())
            );
        }
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException("Invalid currency ID: " + currencyIdParam);
        }
    }

    private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> listSettings)
    {
        for (Setting setting : listSettings)
        {
            String value = request.getParameter(setting.getKey());
            if (value != null)
            {
                setting.setValue(value);
            }
        }
        service.saveAll(listSettings);
    }

    @PostMapping("/settings/save_mail_server")
    public String saveMailServerSettings(HttpServletRequest request, RedirectAttributes ra)
    {
        List<Setting> mailServerSettings = service.getMailServerSettings();
        updateSettingValuesFromForm(request, mailServerSettings);

        ra.addFlashAttribute("message", "Mail server settings have been saved");

        return "redirect:/settings#mailServer";
    }

    @PostMapping("/settings/save_mail_templates")
    public String saveMailTemplateSettings(HttpServletRequest request, RedirectAttributes ra)
    {
        List<Setting> mailTemplateSettings = service.getMailTemplateSettings();
        updateSettingValuesFromForm(request, mailTemplateSettings);

        ra.addFlashAttribute("message", "Mail template settings have been saved");

        return "redirect:/settings#mailTemplates";
    }

    @PostMapping("/settings/save_payment")
    public String savePaymentSettings(HttpServletRequest request, RedirectAttributes ra)
    {
        List<Setting> paymentSettings = service.getPaymentSettings();
        updateSettingValuesFromForm(request, paymentSettings);

        ra.addFlashAttribute("message", "Payment settings have been saved");

        return "redirect:/settings#payment";
    }
}