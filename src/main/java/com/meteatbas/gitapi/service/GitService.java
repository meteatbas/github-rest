package com.meteatbas.gitapi.service;

import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class GitService {

    @Value("${demo.name}")
    private String name;

    @Value("${demo.email}")
    private String email;


    public ResponseEntity createGitCommit(MultipartFile multipartFile ) throws GitAPIException, IOException {
        try {


            // Create a few new files

            File localpath = new File("C:/Users/meteatbas/Desktop/Döküman/projectss/");
            Git git = Git.init().setDirectory(localpath).call();

//            git.commit().setMessage("Initial commit Mete").call();
//            System.out.println("Committed file " + localpath + " to repository at " + git.getRepository().getDirectory());
            System.out.println("yaml value "+name+email);

            System.out.println("Created repository: " + git.getRepository().getDirectory());
            File myFile = new File(git.getRepository().getDirectory().getParent(), "testfile");

            git.add().addFilepattern(".").call();

//            TODO:TO ADD FİRST COMMİT
            CommitCommand commitCommand = git.commit().setMessage("init project")
                    .setCommitter(name, email);
            commitCommand.setMessage("initial commit").call();


//            if (!myFile.createNewFile()) {
//                throw new IOException("Could not create file " + myFile);
//            }

            List<Ref> call = git.branchList().call();

            for(Ref ref : call ){
                int i = 1;
                System.out.println("rama" + i + ref);
                i++;
                git = Git.init().setDirectory(localpath).call();
                git.remoteAdd().setUri(new URIish("https://github.com/meteatbas/parcial3")).setName("origin").call();
                git.push().setRemote("https://github.com/meteatbas/parcial3").setCredentialsProvider(new UsernamePasswordCredentialsProvider("meteatbas","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")).setPushAll().add(".").call();
            }
            return new ResponseEntity(myFile.getName(), HttpStatus.CREATED);
        } catch (GitAPIException | URISyntaxException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
}
