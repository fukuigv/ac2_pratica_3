package com.devops.projeto_ac2.application.usecases;

import com.devops.projeto_ac2.domain.entities.Aluno;
import com.devops.projeto_ac2.domain.repositories.AlunoRepository;
import com.devops.projeto_ac2.domain.valueobjects.MediaFinal;
import com.devops.projeto_ac2.domain.valueobjects.NomeAluno;
import com.devops.projeto_ac2.domain.valueobjects.RegistroAcademico;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para ObterRankingAlunosUseCase
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do Use Case Obter Ranking")
class ObterRankingAlunosUseCaseTest {
    
    @Mock
    private AlunoRepository alunoRepository;
    
    @InjectMocks
    private ObterRankingAlunosUseCase useCase;
    
    @Test
    @DisplayName("Deve retornar ranking ordenado por média final")
    void deveRetornarRankingOrdenado() {
        // Arrange
        Aluno aluno1 = criarAlunoComMedia("João", "11111", 8.5);
        aluno1.adicionarCursosExtras(5);
        Aluno aluno2 = criarAlunoComMedia("Maria", "22222", 9.5);
        aluno2.adicionarCursosExtras(6);
        Aluno aluno3 = criarAlunoComMedia("Pedro", "33333", 7.0);
        aluno3.adicionarCursosExtras(4);

        when(alunoRepository.buscarTodos()).thenReturn(Arrays.asList(aluno1, aluno2, aluno3));

        // Act
        List<Aluno> ranking = useCase.executar();

        // Assert
        assertThat(ranking).hasSize(3);
        assertThat(ranking.get(0).getNome()).isEqualTo("Maria");
        assertThat(ranking.get(1).getNome()).isEqualTo("João");
        assertThat(ranking.get(2).getNome()).isEqualTo("Pedro");

        verify(alunoRepository, times(1)).buscarTodos();
    }

    @Test
    @DisplayName("Deve retornar top N alunos")
    void deveRetornarTopN() {
        // Arrange
        Aluno aluno1 = criarAlunoComMedia("João", "11111", 8.5);
        aluno1.adicionarCursosExtras(5);
        Aluno aluno2 = criarAlunoComMedia("Maria", "22222", 9.5);
        aluno2.adicionarCursosExtras(6);
        Aluno aluno3 = criarAlunoComMedia("Pedro", "33333", 7.0);
        aluno3.adicionarCursosExtras(4);

        when(alunoRepository.buscarTodos()).thenReturn(Arrays.asList(aluno1, aluno2, aluno3));

        // Act
        List<Aluno> top2 = useCase.executarTop(2);

        // Assert
        assertThat(top2).hasSize(2);
        assertThat(top2.get(0).getNome()).isEqualTo("Maria");
        assertThat(top2.get(1).getNome()).isEqualTo("João");

        verify(alunoRepository, times(1)).buscarTodos();
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando limite é inválido")
    void deveLancarExcecaoQuandoLimiteInvalido() {
        // Act & Assert
        assertThatThrownBy(() -> useCase.executarTop(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Limite deve ser maior que zero");
        
        assertThatThrownBy(() -> useCase.executarTop(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Limite deve ser maior que zero");
    }
    
    @Test
    @DisplayName("Deve retornar ranking apenas de aprovados")
    void deveRetornarRankingAprovados() {
        // Arrange
        Aluno aprovado1 = criarAlunoConcluidoComMedia("João", "11111", 8.5);
        aprovado1.adicionarCursosExtras(4);
        Aluno aprovado2 = criarAlunoConcluidoComMedia("Maria", "22222", 9.5);
        aprovado2.adicionarCursosExtras(6);
        Aluno reprovado = criarAlunoConcluidoComMedia("Pedro", "33333", 4.0);
        aprovado1.adicionarCursosExtras(3);

        when(alunoRepository.buscarConcluidos()).thenReturn(Arrays.asList(aprovado1, aprovado2, reprovado));

        // Act
        List<Aluno> rankingAprovados = useCase.executarAprovados();

        // Assert
        assertThat(rankingAprovados).hasSize(2);
        assertThat(rankingAprovados.get(0).getNome()).isEqualTo("Maria");
        assertThat(rankingAprovados.get(1).getNome()).isEqualTo("João");

        verify(alunoRepository, times(1)).buscarConcluidos();
    }

    @Test
    @DisplayName("Deve ordenar por cursos adicionais quando médias são iguais")
    void deveOrdenarPorCursosAdicionaisQuandoMediasIguais() {
        // Arrange
        Aluno aluno1 = criarAlunoComMedia("João", "11111", 8.0);
        aluno1.adicionarCursosExtras(3);

        Aluno aluno2 = criarAlunoComMedia("Maria", "22222", 8.0);
        aluno2.adicionarCursosExtras(5);

        when(alunoRepository.buscarTodos()).thenReturn(Arrays.asList(aluno1, aluno2));

        // Act
        List<Aluno> ranking = useCase.executar();

        // Assert
        assertThat(ranking.get(0).getNome()).isEqualTo("Maria");
        assertThat(ranking.get(1).getNome()).isEqualTo("João");
    }

    private Aluno criarAlunoComMedia(String nome, String ra, double media) {
        Aluno aluno = Aluno.criar(
                NomeAluno.criar(nome), 
                RegistroAcademico.criar(ra)
        );
        aluno.atualizarMedia(MediaFinal.criar(media));
        return aluno;
    }
    
    private Aluno criarAlunoConcluidoComMedia(String nome, String ra, double media) {
        Aluno aluno = Aluno.criar(
                NomeAluno.criar(nome), 
                RegistroAcademico.criar(ra)
        );
        aluno.registrarTentativa(MediaFinal.criar(media));
        aluno.concluirCurso(MediaFinal.criar(media));
        return aluno;
    }
}
