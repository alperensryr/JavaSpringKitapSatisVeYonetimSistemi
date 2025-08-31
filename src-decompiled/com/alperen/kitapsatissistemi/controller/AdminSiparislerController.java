/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.controller.AdminSiparislerController
 *  com.alperen.kitapsatissistemi.entity.Siparis
 *  com.alperen.kitapsatissistemi.exception.BusinessException
 *  com.alperen.kitapsatissistemi.service.KullaniciService
 *  com.alperen.kitapsatissistemi.service.SiparisService
 *  com.alperen.kitapsatissistemi.util.SessionUtil
 *  javax.servlet.http.HttpSession
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.data.domain.Page
 *  org.springframework.data.domain.PageRequest
 *  org.springframework.data.domain.Pageable
 *  org.springframework.data.domain.Sort
 *  org.springframework.data.domain.Sort$Direction
 *  org.springframework.stereotype.Controller
 *  org.springframework.ui.Model
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.servlet.mvc.support.RedirectAttributes
 */
package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Siparis;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.service.KullaniciService;
import com.alperen.kitapsatissistemi.service.SiparisService;
import com.alperen.kitapsatissistemi.util.SessionUtil;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/admin/siparisler"})
public class AdminSiparislerController {
    @Autowired
    private SiparisService siparisService;
    @Autowired
    private KullaniciService kullaniciService;
    @Autowired
    private SessionUtil sessionUtil;

    @GetMapping
    public String index(Model model, HttpSession session, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size, @RequestParam(defaultValue="id") String sortBy, @RequestParam(defaultValue="desc") String sortDir, @RequestParam(required=false) String durum) {
        String accessCheck = this.sessionUtil.checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        try {
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort sort = Sort.by((Sort.Direction)direction, (String[])new String[]{sortBy});
            PageRequest pageable = PageRequest.of((int)page, (int)size, (Sort)sort);
            Page siparisPage = durum != null && !durum.trim().isEmpty() ? this.siparisService.findByDurum(durum.trim(), (Pageable)pageable) : this.siparisService.findAll((Pageable)pageable);
            model.addAttribute("siparisler", (Object)siparisPage.getContent());
            model.addAttribute("currentPage", (Object)page);
            model.addAttribute("totalPages", (Object)siparisPage.getTotalPages());
            model.addAttribute("totalElements", (Object)siparisPage.getTotalElements());
            model.addAttribute("sortBy", (Object)sortBy);
            model.addAttribute("sortDir", (Object)sortDir);
            model.addAttribute("durum", (Object)durum);
        }
        catch (BusinessException e) {
            model.addAttribute("errorMessage", (Object)e.getMessage());
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", (Object)("Sipari\u015fler y\u00fcklenirken hata olu\u015ftu: " + e.getMessage()));
        }
        model.addAttribute("title", (Object)"Sipari\u015f Y\u00f6netimi");
        return "admin/siparisler/index";
    }

    @GetMapping(value={"/details/{id}"})
    public String details(@PathVariable Long id, Model model, HttpSession session) {
        block5: {
            String accessCheck = this.sessionUtil.checkAdminAccess(session);
            if (accessCheck != null) {
                return accessCheck;
            }
            Optional siparisOpt = this.siparisService.findById(id);
            if (!siparisOpt.isPresent()) break block5;
            model.addAttribute("siparis", siparisOpt.get());
            model.addAttribute("title", (Object)"Sipari\u015f Detaylar\u0131");
            return "admin/siparisler/details";
        }
        try {
            model.addAttribute("errorMessage", (Object)"Sipari\u015f bulunamad\u0131.");
            return "redirect:/admin/siparisler";
        }
        catch (BusinessException e) {
            model.addAttribute("errorMessage", (Object)e.getMessage());
            return "redirect:/admin/siparisler";
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", (Object)("Sipari\u015f y\u00fcklenirken hata olu\u015ftu: " + e.getMessage()));
            return "redirect:/admin/siparisler";
        }
    }

    @PostMapping(value={"/update-status/{id}"})
    public String updateStatus(@PathVariable Long id, @RequestParam(value="status") String yeniDurum, HttpSession session, RedirectAttributes redirectAttributes) {
        String accessCheck = this.sessionUtil.checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        try {
            Optional siparisOpt = this.siparisService.findById(id);
            if (siparisOpt.isPresent()) {
                Siparis siparis = (Siparis)siparisOpt.get();
                siparis.setDurum(yeniDurum);
                this.siparisService.save(siparis);
                redirectAttributes.addFlashAttribute("successMessage", (Object)"Sipari\u015f durumu ba\u015far\u0131yla g\u00fcncellendi.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", (Object)"Sipari\u015f bulunamad\u0131.");
            }
        }
        catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)e.getMessage());
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)("Sipari\u015f durumu g\u00fcncellenirken hata olu\u015ftu: " + e.getMessage()));
        }
        return "redirect:/admin/siparisler";
    }

    @PostMapping(value={"/delete/{id}"})
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        String accessCheck = this.sessionUtil.checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        try {
            this.siparisService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", (Object)"Sipari\u015f ba\u015far\u0131yla silindi.");
        }
        catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)e.getMessage());
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)("Sipari\u015f silinirken hata olu\u015ftu: " + e.getMessage()));
        }
        return "redirect:/admin/siparisler";
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @PostMapping(value={"/bulk/{action}"})
    public String bulkUpdateStatus(@PathVariable String action, @RequestParam(value="siparisIds") List<Long> siparisIds, HttpSession session, RedirectAttributes redirectAttributes) {
        String accessCheck = this.sessionUtil.checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        try {
            String yeniDurum;
            switch (action.toLowerCase()) {
                case "approve": {
                    yeniDurum = "ONAYLANDI";
                    break;
                }
                case "ship": {
                    yeniDurum = "KARGOLANDI";
                    break;
                }
                case "deliver": {
                    yeniDurum = "TESLIM_EDILDI";
                    break;
                }
                case "cancel": {
                    yeniDurum = "IPTAL_EDILDI";
                    break;
                }
                default: {
                    redirectAttributes.addFlashAttribute("errorMessage", (Object)"Ge\u00e7ersiz i\u015flem.");
                    return "redirect:/admin/siparisler";
                }
            }
            this.siparisService.bulkUpdateSiparisDurum(siparisIds, yeniDurum);
            redirectAttributes.addFlashAttribute("successMessage", (Object)"Toplu g\u00fcncelleme ba\u015far\u0131l\u0131.");
            return "redirect:/admin/siparisler";
        }
        catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)e.getMessage());
            return "redirect:/admin/siparisler";
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)("Toplu g\u00fcncelleme s\u0131ras\u0131nda hata olu\u015ftu: " + e.getMessage()));
        }
        return "redirect:/admin/siparisler";
    }
}

