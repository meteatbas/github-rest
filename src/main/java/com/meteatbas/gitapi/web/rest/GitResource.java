package com.meteatbas.gitapi.web.rest;

import com.meteatbas.gitapi.service.GitService;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/git/projects")
public class GitResource {

    @Autowired
    private final GitService gitService;


    public GitResource(GitService gitService) {
        this.gitService = gitService;
    }

    @PostMapping("/create")
    public ResponseEntity createProject(
            @RequestParam("file") MultipartFile multipartFile) throws GitAPIException, IOException {

        return this.gitService.createGitCommit(multipartFile );
    }

    @PutMapping("/update")
    public ResponseEntity updateProject(
            @RequestParam("file") MultipartFile multipartFile) throws GitAPIException, IOException {

        return this.gitService.updateGitCommit(multipartFile );
    }
}
