package com.hoaxify.webservice.hoax;

import com.hoaxify.webservice.error.NotFoundException;
import com.hoaxify.webservice.file.FileAttachment;
import com.hoaxify.webservice.file.FileAttachmentRepository;
import com.hoaxify.webservice.file.FileService;
import com.hoaxify.webservice.hoax.vm.HoaxSubmitVM;
import com.hoaxify.webservice.user.UserService;
import com.hoaxify.webservice.user.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HoaxService {

    final HoaxRepository hoaxRepository;

    final FileAttachmentRepository fileAttachmentRepository;

    UserService userService;

    FileService fileService;

    public HoaxService(FileService fileService, HoaxRepository hoaxRepository, FileAttachmentRepository fileAttachmentRepository, UserService userService) {
        this.fileService = fileService;
        this.hoaxRepository = hoaxRepository;
        this.fileAttachmentRepository = fileAttachmentRepository;
        this.userService = userService;
    }

    public void saveHoax(HoaxSubmitVM hoaxSubmitVM, Users user) {
        Hoaxes hoax = new Hoaxes();
        hoax.setContent(hoaxSubmitVM.getContent());
        hoax.setUser(user);
        hoaxRepository.save(hoax);

        Optional<FileAttachment> fileAttachment = fileAttachmentRepository.findById(hoaxSubmitVM.getAttachmentId());
        if(fileAttachment.isPresent()){
            fileAttachment.get().setHoax(hoax);
            fileAttachmentRepository.save(fileAttachment.get());
        }
    }

    public Page<Hoaxes> getHoaxList(Pageable page){
        return hoaxRepository.findAll(page);
    }

    public Page<Hoaxes> getOldHoaxes(long id, String username, Pageable page) {
        Specification<Hoaxes> specification = Specification.where(idLessThan(id));
        if(username != null){
            Users inDB = userService.getUserByUserName(username);
            specification = specification.and(userIs(inDB));
            return hoaxRepository.findAll(specification, page);
        }
        return hoaxRepository.findAll(specification, page);
    }

    public Page<Hoaxes> getHoaxesOfUser(String username, Pageable page){
        Users inDB = userService.getUserByUserName(username);
        return hoaxRepository.findByUser(inDB, page);
    }

    public long getNewHoaxesCount(long id, String username) {
        Specification<Hoaxes> specification = Specification.where(idGreaterThan(id));
        if(username != null){
            Users inDB = userService.getUserByUserName(username);
            specification = specification.and(userIs(inDB));
        }
        return hoaxRepository.count(specification);
    }

    public List<Hoaxes> getNewHoaxes(long id, String username, Sort sort) {
        if(username != null){
            Users inDB = userService.getUserByUserName(username);
            return hoaxRepository.findTop10ByIdGreaterThanAndUser(id, inDB, sort);
        }
        return hoaxRepository.findTop10ByIdGreaterThan(id, sort);
    }

    private Specification<Hoaxes> idLessThan(long id){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("id"), id);
    }

    private Specification<Hoaxes> userIs(Users user){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("user"), user);
    }

    private Specification<Hoaxes> idGreaterThan(long id){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("id"), id);
    }

    public void delete(long id) {
        Hoaxes hoax = hoaxRepository.findById(id).orElseThrow(NotFoundException::new);
        if(hoax.getFileAttachment() != null) {
            try {
                fileService.deleteFile(hoax.getFileAttachment().getName(), 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        hoaxRepository.deleteById(id);
    }

}
