package fr.gatay.cedric.repository;

import fr.gatay.cedric.model.Dialog;
import fr.gatay.cedric.model.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {

    @Query("from Message message WHERE message.dialog = :dialog ORDER BY message.creationDate ASC")
    List<Message> findByDialog(@Param("dialog") Dialog dialog);

    Message findFirstByDialogOrderByCreationDateDesc(@Param("dialog") Dialog dialog);
}
