package com.chronos.agenda.api

import com.chronos.agenda.api.model.AlunoResult
import com.chronos.agenda.api.service.AlunoService
import com.chronos.agenda.model.Aluno
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by John Vanderson M L on 26/02/2018.
 */
class AgendaAPI {

    private val builder: Retrofit.Builder
    private val logging = HttpLoggingInterceptor()
    private val httpClient = OkHttpClient.Builder()
    private val alunoService: AlunoService

    init {
        val gson = GsonBuilder().setLenient().create()
        builder = Retrofit.Builder()
                .baseUrl("http://192.168.112.102:8080/api/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
        logging.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(logging)

        alunoService = createService(AlunoService::class.java)

    }



    fun <S> createService(serviceClass: Class<S>): S {
        val httpClientBuild = httpClient.connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build()
        builder.client(httpClientBuild)

        val retrofit: Retrofit = builder.build()




        return retrofit.create(serviceClass)
    }

    fun salvarAluno(aluno: Aluno): Observable<AlunoResult> {
        return alunoService.salvar(aluno)

    }

    fun buscarAlunos():Observable<AlunoResult>{
        return alunoService.buscarAlunos()
    }

    fun deletarAluno(id:String):Observable<Unit>{
        return  alunoService.deletar(id)
    }

}