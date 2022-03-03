package com.meteatbas.gitapi.service;

import com.meteatbas.gitapi.config.ApplicationProperties;
import com.meteatbas.gitapi.config.GitConfiguration;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class GitService {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    GitConfiguration gitConfiguration;

    public ResponseEntity createGitCommit(MultipartFile multipartFile) throws GitAPIException, IOException {
        try {
//            Git.cloneRepository()
//                    .setURI(applicationProperties.getRepositoryName())
//                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider("meteatbas", "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"))
//                    .setDirectory(new File("//desktop-6ujvr85/Users/meteatbas/Documents/projectSix/"))
//                    .call();
            // Create a few new files

            File localpath = new File(applicationProperties.getFilePath());
            Git git = Git.init().setDirectory(localpath).call();

//            git.commit().setMessage("Initial commit Mete").call();
//            System.out.println("Committed file " + localpath + " to repository at " + git.getRepository().getDirectory());
            System.out.println("yaml value " + applicationProperties.getName() + applicationProperties.getEmail());

            System.out.println("Created repository: " + git.getRepository().getDirectory());
            File myFile = new File(git.getRepository().getDirectory().getParent(), "testfile");

            git.add().addFilepattern(".").call();

//            TODO:TO ADD FİRST COMMİT
            CommitCommand commitCommand = git.commit().setMessage("init project")
                    .setCommitter(applicationProperties.getName(), applicationProperties.getEmail());
            commitCommand.setMessage("initial commit").call();

//            if (!myFile.createNewFile()) {
//                throw new IOException("Could not create file " + myFile);
//            }

            List<Ref> call = git.branchList().call();

            for (Ref ref : call) {
                int i = 1;
                System.out.println("rama" + i + ref);
                i++;
                git = Git.init().setDirectory(localpath).call();
                git.remoteAdd().setUri(new URIish(applicationProperties.getRepositoryName())).setName("origin").call();
                git.push().setRemote(applicationProperties.getRepositoryName()).setCredentialsProvider(new UsernamePasswordCredentialsProvider(applicationProperties.getGitUsername(), applicationProperties.getGitPassword())).setPushAll().add(".").call();
            }
            return new ResponseEntity(myFile.getName(), HttpStatus.CREATED);
        } catch (GitAPIException | URISyntaxException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    public ResponseEntity updateGitCommit(MultipartFile multipartFile) throws IOException, GitAPIException {

        Set fileList;
        fileList = listFilesUsingDirectoryStream(applicationProperties.getFilePath());

        if (fileList.contains(multipartFile.getOriginalFilename())) {

            gitConfiguration.git(applicationProperties).pull().call();

            System.out.println("yaml value " + applicationProperties.getName() + applicationProperties.getEmail() + applicationProperties.getRepositoryName());
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        } else {
            try {

                File localpath = new File(applicationProperties.getFilePath());
                File filePath = new File(applicationProperties.getFilePath(), multipartFile.getOriginalFilename());

                Git git = Git.init().setDirectory(localpath).call();
//                gitConfiguration.git().=Git.init().setDirectory(localpath).call();
                System.out.println("Git configuration : " + gitConfiguration.git(applicationProperties).getRepository());

                gitConfiguration.git(applicationProperties).pull().call();

                FileCopyUtils.copy(multipartFile.getInputStream(), new FileOutputStream(filePath));

//                System.out.println("Created repository: " + git.getRepository().getDirectory());
                System.out.println("path directory : " + listFilesUsingDirectoryStream(applicationProperties.getFilePath()));

                gitConfiguration.git(applicationProperties).add().addFilepattern(".").call();

                CommitCommand commitCommand = gitConfiguration.git(applicationProperties).commit().setMessage("init project")
                        .setCommitter(applicationProperties.getName(), applicationProperties.getEmail());
                commitCommand.setMessage("initial commit").call();

                List<Ref> call = git.branchList().call();

                try (git) {
                    Iterable<RevCommit> commits = git.log().all().call();
                    int count = 0;
                    for (RevCommit commit : commits) {
                        System.out.println("LogCommit: " + commit);
                        count++;
                    }
                    System.out.println(count);
                }

                for (Ref ref : call) {
                    int i = 1;
                    System.out.println("rama" + i + ref);
                    i++;
//                    git = Git.init().setDirectory(localpath).call();
                    gitConfiguration.git(applicationProperties).remoteAdd().setUri(new URIish(applicationProperties.getRepositoryName())).setName("origin").call();
//                  git.fetch().setRemote("https://github.com/meteatbas/parcial3").setRemote("origin").call();
                    gitConfiguration.git(applicationProperties).push().setRemote(applicationProperties.getRepositoryName()).setCredentialsProvider(new UsernamePasswordCredentialsProvider(applicationProperties.getGitUsername(), applicationProperties.getGitPassword())).setPushAll().add(".").call();
                }
                return new ResponseEntity(HttpStatus.ACCEPTED);
            } catch (GitAPIException | URISyntaxException e) {
                e.printStackTrace();
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
        }
    }

    public Set<String> listFilesUsingDirectoryStream(String dir) throws IOException {
        Set<String> fileList = new HashSet<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    fileList.add(path.getFileName()
                            .toString());
                }
            }
        }
        return fileList;
    }
}
