package com.chronos.agenda.api.model

import com.chronos.agenda.model.Aluno

/**
 * Created by John Vanderson M L on 26/02/2018.
 */

data class AlunoResult(val momentoDaUltimaModificacao: String, val alunos:List<Aluno>)