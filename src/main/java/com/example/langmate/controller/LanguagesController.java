package com.example.langmate.controller;

import com.example.langmate.controller.payload.response.GetLanguageResponse;
import com.example.langmate.controller.payload.response.GetLanguagesResponse;
import com.example.langmate.service.impl.LanguageService;
import com.example.langmate.validation.LangmateRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/langmate/language")
@RequiredArgsConstructor
@Slf4j
public class LanguagesController {

  private final LanguageService languageService;

  @GetMapping(path = "/all")
  public String getLanguages(
      Model model, 
      @PageableDefault(size = 5, sort = "name") Pageable pageable,
      @RequestParam(required = false) String sort) throws LangmateRuntimeException {
    
    log.info("Received request for languages with pagination - page: {}, size: {}, sort: {}", 
             pageable.getPageNumber(), pageable.getPageSize(), sort);
    
    Page<GetLanguageResponse> languagesPage = languageService.getLanguagesPaginated(pageable);
    
    // Create complete pagination data structure
    List<PageInfo> pageNumbers = new ArrayList<>();
    for (int i = 0; i < languagesPage.getTotalPages(); i++) {
      pageNumbers.add(new PageInfo(i, i + 1));
    }
    
    // Pre-calculate pagination values to avoid arithmetic in template
    int currentPage = languagesPage.getNumber();
    int prevPage = Math.max(0, currentPage - 1);
    int nextPage = Math.min(languagesPage.getTotalPages() - 1, currentPage + 1);
    int currentPagePlusOne = currentPage + 1;
    
    // Pre-calculate sort links to avoid Thymeleaf arithmetic
    String currentSort = sort != null ? sort : "name,asc";
    String ascSort = "name,asc";
    String descSort = "name,desc";
    
    log.info("Adding {} languages to model for page {} of {}", 
             languagesPage.getContent().size(), 
             languagesPage.getNumber(), 
             languagesPage.getTotalPages());
    
    model.addAttribute("languagesPage", languagesPage);
    model.addAttribute("pageNumbers", pageNumbers);
    model.addAttribute("prevPage", prevPage);
    model.addAttribute("nextPage", nextPage);
    model.addAttribute("currentPagePlusOne", currentPagePlusOne);
    model.addAttribute("currentSort", currentSort);
    model.addAttribute("ascSort", ascSort);
    model.addAttribute("descSort", descSort);
    return "allLanguages";
  }

  @PostMapping(path = "/enroll/{languageName}")
  public String enrollInLanguage(@PathVariable String languageName, Model model) {
    model.addAttribute("language", languageName);
    return "enroll";
  }
  
  // Helper class for page information
  public static class PageInfo {
    private final int pageNumber;
    private final int displayNumber;
    
    public PageInfo(int pageNumber, int displayNumber) {
      this.pageNumber = pageNumber;
      this.displayNumber = displayNumber;
    }
    
    public int getPageNumber() {
      return pageNumber;
    }
    
    public int getDisplayNumber() {
      return displayNumber;
    }
  }
}
