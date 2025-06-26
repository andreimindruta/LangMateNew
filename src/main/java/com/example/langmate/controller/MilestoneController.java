package com.example.langmate.controller;

import com.example.langmate.controller.payload.request.MilestoneRequest;
import com.example.langmate.controller.payload.response.GetMilestoneResponse;
import com.example.langmate.domain.entities.Milestone;
import com.example.langmate.domain.entities.MilestoneTargetType;
import com.example.langmate.service.impl.MilestoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/langmate/milestones")
public class MilestoneController {

    @Autowired
    private MilestoneService milestoneService;

    @GetMapping
    public ResponseEntity<List<GetMilestoneResponse>> getAllMilestones() {
        List<Milestone> milestones = milestoneService.getAllMilestones();
        List<GetMilestoneResponse> responses = milestones.stream()
                .map(milestone -> new GetMilestoneResponse(
                        milestone.getId(),
                        milestone.getName(),
                        milestone.getDescription(),
                        milestone.getTargetValue(),
                        milestone.getTargetType().name()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetMilestoneResponse> getMilestoneById(@PathVariable Long id) {
        Milestone milestone = milestoneService.getMilestoneById(id);
        GetMilestoneResponse response = new GetMilestoneResponse(
                milestone.getId(),
                milestone.getName(),
                milestone.getDescription(),
                milestone.getTargetValue(),
                milestone.getTargetType() != null ? milestone.getTargetType().name() : null

        );
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<GetMilestoneResponse> createMilestone(@RequestBody MilestoneRequest request) {
        Milestone milestone = milestoneService.createMilestone(request);
        GetMilestoneResponse response = new GetMilestoneResponse(
                milestone.getId(),
                milestone.getName(),
                milestone.getDescription(),
                milestone.getTargetValue(),
                milestone.getTargetType().name()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMilestone(@PathVariable Long id) {
        milestoneService.deleteMilestone(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Milestone>> getMilestoneByUserId(@PathVariable Long userId) {
        List <Milestone> milestones = milestoneService.getUserMilestones(userId);
        return ResponseEntity.ok(milestones);
    }

    // Web: Afișare pagină milestones cu listă și formular
    @GetMapping("/web")
    public String showMilestonesPage(Model model) {
        List<Milestone> milestones = milestoneService.getAllMilestones();
        model.addAttribute("milestones", milestones);
        model.addAttribute("milestoneForm", new MilestoneRequest(null, null, null, null));
        model.addAttribute("targetTypes", MilestoneTargetType.values());
        return "milestones";
    }

    // Web: Preluare date din formular și creare milestone
    @PostMapping("/create-web")
    public String createMilestoneWeb(@ModelAttribute MilestoneRequest milestoneForm) {
        milestoneService.createMilestone(milestoneForm);
        return "redirect:/langmate/milestones/web";
    }

    // Web: Populează formularul cu datele milestone-ului selectat pentru editare
    @GetMapping("/edit/{id}")
    public String editMilestoneForm(@PathVariable Long id, Model model) {
        Milestone milestone = milestoneService.getMilestoneById(id);
        MilestoneRequest milestoneForm = new MilestoneRequest(
            milestone.getName(),
            milestone.getDescription(),
            milestone.getTargetValue(),
            milestone.getTargetType() != null ? milestone.getTargetType().name() : null
        );
        model.addAttribute("milestones", milestoneService.getAllMilestones());
        model.addAttribute("milestoneForm", milestoneForm);
        model.addAttribute("milestoneId", id);
        model.addAttribute("targetTypes", MilestoneTargetType.values());
        return "milestones";
    }

    // Web: Update milestone
    @PostMapping("/update-web/{id}")
    public String updateMilestoneWeb(@PathVariable Long id, @ModelAttribute MilestoneRequest milestoneForm) {
        milestoneService.updateMilestone(id, milestoneForm);
        return "redirect:/langmate/milestones/web";
    }

    // Web: Delete milestone
    @PostMapping("/delete-web/{id}")
    public String deleteMilestoneWeb(@PathVariable Long id) {
        milestoneService.deleteMilestone(id);
        return "redirect:/langmate/milestones/web";
    }
}
