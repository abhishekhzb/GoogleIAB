package papaya.googleiab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.android.billingclient.api.*

class MainActivity : AppCompatActivity() {

    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.buyNow)

        val purchasesUpdatedListener = PurchasesUpdatedListener{
                billingResult, purchses ->
        }

        val billingClient = BillingClient.newBuilder(this)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases().build()

        button.setOnClickListener {

            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingServiceDisconnected() {
//                    TODO("Not yet implemented")
                }

                override fun onBillingSetupFinished(billingResult: BillingResult) {
//                    TODO("Not yet implemented")

                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK){

                        val productList =
                            listOf(
                                QueryProductDetailsParams.Product.newBuilder()
                                    .setProductId("test_prod_1")
                                    .setProductType(BillingClient.ProductType.INAPP)
                                    .build()
                            )

                        val params = QueryProductDetailsParams.newBuilder().setProductList(productList).build()

                        billingClient.queryProductDetailsAsync(params) {
                                billingResult,
                                productDetailsList ->

                            Log.e("responseCode",""+productDetailsList)
                            for (productDetails in productDetailsList) {
                                val productDetailsParamsList =
                                    listOf(
                                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                            .setProductDetails(productDetails)
                                            //.setofferToken(offerToken)
                                            .build()
                                    )
                                val billingFlowParams =
                                    BillingFlowParams.newBuilder()
                                        .setProductDetailsParamsList(productDetailsParamsList).build()

                                // Launch the billing flow
                                val responseCode = billingClient.launchBillingFlow(this@MainActivity, billingFlowParams).responseCode
                                Log.e("responseCode",""+responseCode)
                            }
                        }
                    }
                }

            })
        }
    }
}

