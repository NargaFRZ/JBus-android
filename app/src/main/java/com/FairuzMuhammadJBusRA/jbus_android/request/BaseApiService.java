package com.FairuzMuhammadJBusRA.jbus_android.request;

import com.FairuzMuhammadJBusRA.jbus_android.model.Account;
import com.FairuzMuhammadJBusRA.jbus_android.model.BaseResponse;
import com.FairuzMuhammadJBusRA.jbus_android.model.Bus;
import com.FairuzMuhammadJBusRA.jbus_android.model.BusType;
import com.FairuzMuhammadJBusRA.jbus_android.model.Facility;
import com.FairuzMuhammadJBusRA.jbus_android.model.Payment;
import com.FairuzMuhammadJBusRA.jbus_android.model.Renter;
import com.FairuzMuhammadJBusRA.jbus_android.model.Station;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BaseApiService {
    @GET("account/{id}")
    Call<Account> getAccountbyId (@Path("id") int id);

    @POST("account/register")
    Call<BaseResponse<Account>> register(
            @Query("name") String name,
            @Query("email") String email,
            @Query("password") String password);

    @POST("account/login")
    Call<BaseResponse<Account>> login(
            @Query("email") String email,
            @Query("password") String password);

    @POST("account/{id}/topUp")
    Call<BaseResponse<Account>> topUp(
            @Path("id") int id,
            @Query("amount") double amount);

    @POST("account/{id}/registerRenter")
    Call<BaseResponse<Renter>> registerRenter(
            @Path("id") int id,
            @Query("companyName") String companyName,
            @Query("address") String address,
            @Query("phoneNumber") String phoneNumber);

    @GET("bus/getMyBus")
    Call<List<Bus>> getMyBus(
            @Query("accountId") int accountId);

    @POST("bus/create")
    Call<BaseResponse<Bus>> create(
            @Query("accountId") int accountId,
            @Query("name") String name,
            @Query("capacity") int capacity,
            @Query("facilities") List<Facility> facilities,
            @Query("busType") BusType busType,
            @Query("price") int price,
            @Query("stationDepartureId") int stationDepartureId,
            @Query("stationArrivalId") int stationArrivalId);

    @GET("station/getAll")
    Call<List<Station>> getAllStation();

    @GET("bus/page")
    Call<List<Bus>> getBus(@Query("page") int page, @Query("size") int pageSize);

    @GET("bus/numberOfBuses")
    Call<Integer> numberOfBuses();

    @POST("bus/addSchedule")
    Call<BaseResponse<Bus>> addSchedule(@Query("busId") int busId,
                                        @Query("time") String time);

    @GET("bus/{id}")
    Call<Bus> getBusbyId(@Path("id") int busId);

    @GET("bus/seats")
    Call<Map<String,Boolean>> getSeats(@Query("busId") int busId,
                                       @Query("date") String date);

    @POST("payment/makeBooking")
    Call<BaseResponse<Payment>> makeBooking(
            @Query("buyerId") int buyerId,
            @Query("renterId") int renterId,
            @Query("busId") int busId,
            @Query("busSeats") List<String> busSeats,
            @Query("departureDate") String departureDate
    );

    @GET("payment/getAccountPayment")
    Call<List<Payment>> getBuyerPayment(
            @Query("accountId") int accountId
    );

    @GET("payment/getRenterPayment")
    Call<List<Payment>> getRenterPayment(
            @Query("renterId") int renterId
    );

    @POST("payment/{id}/cancel")
    Call<BaseResponse<Payment>> cancel(
            @Path("id") int id
    );
    @POST("payment/{id}/accept")
    Call<BaseResponse<Payment>> accept(
            @Path("id") int id
    );
}