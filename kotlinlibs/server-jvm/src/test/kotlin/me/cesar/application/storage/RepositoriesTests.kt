package me.cesar.application.storage

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull
import java.util.Date

@DataJpaTest
class RepositoriesTests @Autowired constructor(
    val entityManager: TestEntityManager,
    val articleRepository: ArticleRepository
) {

    @Test
    fun `When findByIdOrNull then return Article`() {
        val article = Article("Title 2", "blog2", "B", Date())
        entityManager.persist(article)
        entityManager.flush()
        val found = articleRepository.findByIdOrNull(article.id!!)
        Assertions.assertThat(found).isEqualTo(article)
    }
}
