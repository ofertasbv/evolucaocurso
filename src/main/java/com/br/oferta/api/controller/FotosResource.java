package com.br.oferta.api.controller;

import com.br.oferta.api.util.storage.Disco;
import com.br.oferta.api.util.upload.exception.MyFileNotFoundException;
import com.br.oferta.api.util.upload.payload.UploadFileResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/fotos")
public class FotosResource {

    @Autowired
    private Disco disco;

    @Value("${contato.disco.raiz}")
    private Path local;

    private Path fileStorageLocation;

    private final Logger logger = LoggerFactory.getLogger(FotosResource.class);

    @RequestMapping("/nova")
    public ModelAndView nova() {
        ModelAndView mv = new ModelAndView("paginas/arquivo/create");
        return mv;
    }

    @RequestMapping("/table")
    public ModelAndView table() {
        ModelAndView mv = new ModelAndView("paginas/arquivo/table");
        return mv;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ModelAndView uploadFile1(MultipartFile file) throws IOException, ServletException {

        this.fileStorageLocation = Paths.get(local.toUri()).toAbsolutePath().normalize();
        
//        disco.salvarFoto(file);

        System.out.println("fileStorageLocation: " + fileStorageLocation);

        Path targetLocation = this.fileStorageLocation.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/fotos/download/")
                .path(file.getOriginalFilename())
                .toUriString();

        String url = fileDownloadUri.substring(0, fileDownloadUri.lastIndexOf('/') + 1);
        
        
        UploadFileResponse response = new UploadFileResponse(file.getName(), file.getOriginalFilename(),
                file.getContentType(), file.getSize());

        System.out.println("File: " + file);
        System.out.println("file.getOriginalFilename(): " + file.getOriginalFilename());
        System.out.println("file.getContentType(): " + file.getContentType());
        System.out.println("targetLocation: " + targetLocation);
        System.out.println("fileDownloadUri: " + fileDownloadUri);
        System.out.println("url: " + url);

        System.out.println("Url: " + response.getFileDownloadUri());
        System.out.println("FileName: " + response.getFileName());
        System.out.println("FileType: " + response.getFileType());
        System.out.println("Size: " + response.getSize());

        ModelAndView mv = new ModelAndView("redirect:/fotos/nova");
        mv.addObject("arquivo", response);
        return mv;
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {

        Resource resource = loadFileAsResource(fileName);
        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }
        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
