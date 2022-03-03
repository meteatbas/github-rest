package com.meteatbas.gitapi.config;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class GitConfiguration {

        @Bean
        @Autowired
        public Git git(ApplicationProperties applicationProperties) throws GitAPIException {

            Git git = Git.init().setDirectory(new File(applicationProperties.getFilePath())).call();
            return new Git(git.getRepository());

        }
}
