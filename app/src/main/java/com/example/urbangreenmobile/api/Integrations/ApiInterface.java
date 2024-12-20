package com.example.urbangreenmobile.api.Integrations;

import com.example.urbangreenmobile.api.models.Fornecedor.CreateFornecedorRequest;
import com.example.urbangreenmobile.api.models.Fornecedor.GetFornecedorResponse;
import com.example.urbangreenmobile.api.models.Fornecedor.UpdateFornecedorRequest;
import com.example.urbangreenmobile.api.models.Insumo.CreateInsumoRequest;
import com.example.urbangreenmobile.api.models.Insumo.GetInsumoResponse;
import com.example.urbangreenmobile.api.models.Insumo.UpdateInsumoRequest;
import com.example.urbangreenmobile.api.models.Login.LoginRequest;
import com.example.urbangreenmobile.api.models.Login.LoginResponse;
import com.example.urbangreenmobile.api.models.Pedido.CreatePedidoRequest;
import com.example.urbangreenmobile.api.models.Producao.CreateInspecaoRequest;
import com.example.urbangreenmobile.api.models.Pedido.ItemPedidoRequest;
import com.example.urbangreenmobile.api.models.Producao.GetInspecaoResponse;
import com.example.urbangreenmobile.api.models.Producao.TipoItem;
import com.example.urbangreenmobile.api.models.Producao.UpdateInspecaoRequest;
import com.example.urbangreenmobile.api.models.Produto.GetProdutoResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {
    @POST("Login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("Fornecedor?skip=0&take=50")
    Call<List<GetFornecedorResponse>> getFornecedores();

    @POST("Fornecedor")
    Call<Void> criarFornecedor(@Body CreateFornecedorRequest fornecedor);

    @PUT("Fornecedor/{id}")
    Call<Void> atualizarFornecedor(@Path("id") int id, @Body UpdateFornecedorRequest fornecedor);

    @GET("Produto?skip=0&take=50")
    Call<List<GetProdutoResponse>> getProdutos();

    @Multipart
    @POST("Produto")
    Call<Void> criarProduto(
            @Part("nome") RequestBody nome,
            @Part("quantidade") RequestBody quantidade,
            @Part("valor") RequestBody valor,
            @Part MultipartBody.Part imagemProduto
    );

    @Multipart
    @PUT("Produto/{id}")
    Call<Void> atualizarProduto(
            @Path("id") int id,
            @Part("nome") RequestBody nome,
            @Part("quantidade") RequestBody quantidade,
            @Part("valor") RequestBody valor,
            @Part MultipartBody.Part imagemProduto
    );

    @Multipart
    @PUT("produtos/{id}")
    Call<Void> atualizarProduto(
            @Path("id") int id,
            @Part("nome") RequestBody nome,
            @Part("quantidade") RequestBody quantidade,
            @Part("valor") RequestBody valor
    );

    @GET("Insumo?skip=0&take=50")
    Call<List<GetInsumoResponse>> getInsumos();

    @POST("Insumo")
    Call<Void> criarInsumo(@Body CreateInsumoRequest insumo);

    @PUT("Insumo/{id}")
    Call<Void> atualizarInsumo(@Path("id") int id, @Body UpdateInsumoRequest insumo);

    @GET("Inspecao/produto/{id}")
    Call<GetInspecaoResponse> getInspecaoPorProdutoId(@Path("id") int id);

    @POST("Inspecao")
    Call<Void> criarInspecao(@Body CreateInspecaoRequest inspecao);

    @PUT("Inspecao/{id}")
    Call<Void> atualizarInspecao(@Path("id") int id, @Body UpdateInspecaoRequest inspecao);

    @GET("Inspecao/tipos-itens")
    Call<List<TipoItem>> getTiposDeItens();

    @POST("ItemPedido")
    Call<Void> criarItemPedido(@Body ItemPedidoRequest itemPedido);

    @POST("Pedido")
    Call<Void> criarPedido(@Body CreatePedidoRequest pedido);
}
