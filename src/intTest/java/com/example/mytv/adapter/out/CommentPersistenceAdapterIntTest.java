package com.example.mytv.adapter.out;

import static org.assertj.core.api.BDDAssertions.then;

import com.example.mytv.adapter.out.mongo.comment.CommentDocument;
import com.example.mytv.domain.comment.CommentFixtures;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
public class CommentPersistenceAdapterIntTest {
    @Autowired
    private CommentPersistenceAdapter sut;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void createComment() {
        var comment = CommentFixtures.stub(UUID.randomUUID().toString());

        sut.saveComment(comment);

        then(mongoTemplate.findById(comment.getId(), CommentDocument.class))
            .hasFieldOrPropertyWithValue("id", comment.getId())
            .hasFieldOrPropertyWithValue("text", comment.getText());
    }

    @Test
    void updateComment() {
        var comment = CommentFixtures.stub(UUID.randomUUID().toString());
        mongoTemplate.save(CommentDocument.from(comment));

        comment.updateText("new text");
        sut.saveComment(comment);

        then(mongoTemplate.findById(comment.getId(), CommentDocument.class))
            .hasFieldOrPropertyWithValue("id", comment.getId())
            .hasFieldOrPropertyWithValue("text", "new text");
    }

    @Test
    void deleteComment() {
        var comment = CommentFixtures.stub(UUID.randomUUID().toString());
        mongoTemplate.save(CommentDocument.from(comment));

        sut.deleteComment(comment.getId());

        then(mongoTemplate.findById(comment.getId(), CommentDocument.class))
            .isNull();
    }

    @Test
    void loadComment() {
        var commentId = UUID.randomUUID().toString();
        var comment = CommentFixtures.stub(commentId);
        mongoTemplate.save(CommentDocument.from(comment));

        var result = sut.loadComment(commentId);

        then(result)
            .isPresent()
            .hasValueSatisfying(c -> then(c.getId()).isEqualTo(comment.getId()));
    }
}
