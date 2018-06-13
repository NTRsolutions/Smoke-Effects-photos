package com.dvinfosys.smokeeffectphotomaker.Utils;
/*
    Person : Dhaval Thakor
    Email : dhaval@vakratundasystem.in
    File Name : ApiInterface.java
    Description :
       Interface in which method for Retrofit is initialized
 */



import com.dvinfosys.smokeeffectphotomaker.Response.CreateDialogApiResponse;
import com.dvinfosys.smokeeffectphotomaker.Response.ExitDialogOnlineResponse;
import com.dvinfosys.smokeeffectphotomaker.Response.TopAppListResponse;
import com.dvinfosys.smokeeffectphotomaker.Response.VerticalMoreAppsResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET(AppConfig.ONCREATE_DIALOG_URL)
    Call<CreateDialogApiResponse> API_RESPONSE_CALL();

    @GET(AppConfig.TOP_APPS_URL)
    Call<TopAppListResponse> TOP_APP_LIST_RESPONSE_CALL();

    @GET(AppConfig.VERTICAL_MORE_APPS_URL)
    Call<VerticalMoreAppsResponse> VERTICAL_MORE_APPS_RESPONSE_CALL();

    @GET(AppConfig.EXIT_DIALOG_ONLINE_URL)
    Call<ExitDialogOnlineResponse> EXIT_DIALOG_ONLINE_RESPONSE_CALL();
}
