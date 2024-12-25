package com.example.btl_android.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.DetailProductActivity;
import com.example.btl_android.R;
import com.example.btl_android.UpdateDeleteProductActivity;
import com.example.btl_android.adapter.RecycleViewProductAdapter;
import com.example.btl_android.dal.CartSQLiteHelper;
import com.example.btl_android.dal.CommentSQLiteHelper;
import com.example.btl_android.dal.OrderProductSQLiteHelper;
import com.example.btl_android.dal.OrderSQLiteHelper;
import com.example.btl_android.dal.ProductSQLiteHelper;
import com.example.btl_android.dal.UserSQLiteHelper;
import com.example.btl_android.dal.VoucherSQLiteHelper;
import com.example.btl_android.model.Product;
import com.example.btl_android.util.CommonUtil;

import java.util.List;

public class FragmentHome extends Fragment implements RecycleViewProductAdapter.ItemListener {
    private static final String SHARE_PRE_NAME = "mypref";
    private SearchView searchView;
    private Spinner category, price;
    private TextView title;
    private RecyclerView recyclerView;
    private ProductSQLiteHelper productSQLiteHelper;
    private CartSQLiteHelper cartSQLiteHelper;
    private OrderSQLiteHelper orderSQLiteHelper;
    private OrderProductSQLiteHelper orderProductSQLiteHelper;
    private VoucherSQLiteHelper voucherSQLiteHelper;
    private CommentSQLiteHelper commentSQLiteHelper;
    private UserSQLiteHelper userSQLiteHelper;
    private RecycleViewProductAdapter adapter;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        userSQLiteHelper = new UserSQLiteHelper(getContext());

        List<Product> list = productSQLiteHelper.getAll();
        sharedPreferences = getActivity().getSharedPreferences(SHARE_PRE_NAME,
                Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("id", null);
        title.setText("Tất cả sản phẩm (" + list.size() + " sản phẩm)");
        adapter.setList(list);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,
                false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setItemListener(this);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String cate = category.getItemAtPosition(position).toString();
                List<Product> list;
                if (cate.equalsIgnoreCase("All")) {
                    list = productSQLiteHelper.getAll();
                    title.setText("Tất cả sản phẩm (" + list.size() + " sản phẩm)");
                } else {
                    list = productSQLiteHelper.getProductByCategory(cate);
                    title.setText(String.format("Danh sách sản phẩm của %s (%d sản phẩm)", cate,
                            list.size()));
                }
                adapter.setList(list);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        price.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String p = price.getItemAtPosition(position).toString();
                List<Product> list;
                if (p.equalsIgnoreCase("Price default")) {
                    list = productSQLiteHelper.getAll();
                    title.setText("Tất cả sản phẩm" + " (" + list.size() + " sản phẩm)");
                } else if (p.equalsIgnoreCase("Price increase")) {
                    list = productSQLiteHelper.getAllIncre();
                    title.setText(
                            "Danh sách theo giá tăng dần" + " (" + list.size() + " sản phẩm)");
                } else {
                    list = productSQLiteHelper.getAllDesc();
                    title.setText(
                            "Danh sách theo giá giảm dần" + " (" + list.size() + " sản phẩm)");
                }
                adapter.setList(list);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (query.isEmpty()) {
                    title.setText("Tất cả sản phẩm" + " (" + list.size() + " sản phẩm)");
                    adapter.setList(list);
                    return true;
                }

                if (CommonUtil.isContainXSSorSqlInjection(query)) {
//                    Toast.makeText(getContext(), "Keyword is invalid", Toast.LENGTH_SHORT).show();
                    title.setText("Keyword is invalid");
                    return false;
                }

                List<Product> list1 = productSQLiteHelper.getProductByName(query);
                title.setText("Danh sách ứng với từ khóa \"" + query + "\"" + " (" + list1.size() + " sản phẩm)");
                adapter.setList(list1);
                return true;
            }

            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextChange(String keyword) {

                if (keyword.isEmpty()) {
                    List<Product> list = productSQLiteHelper.getAll();
                    title.setText("Tất cả sản phẩm" + " (" + list.size() + " sản phẩm)");
                    adapter.setList(list);
                    return true;
                }

                if (CommonUtil.isContainXSSorSqlInjection(keyword)) {
//                    Toast.makeText(getContext(), "Keyword is invalid", Toast.LENGTH_SHORT).show();
                    title.setText("Keyword is invalid");
                    return false;
                }

                List<Product> list1 = productSQLiteHelper.getProductByName(keyword);
                title.setText("Danh sách ứng với từ khóa \"" + keyword + "\"" + " (" + list1.size() + " sản phẩm)");
                adapter.setList(list1);
                return true;
            }
        });
    }

    private void initView(View view) {
        searchView = view.findViewById(R.id.keyword);
        category = view.findViewById(R.id.category);
        price = view.findViewById(R.id.price);
        title = view.findViewById(R.id.title);
        recyclerView = view.findViewById(R.id.recyclerView);
        productSQLiteHelper = new ProductSQLiteHelper(getContext());
        adapter = new RecycleViewProductAdapter();

        String[] arr = getResources().getStringArray(R.array.category);
        String[] arr1 = new String[arr.length + 1];
        arr1[0] = "All";
        System.arraycopy(arr, 0, arr1, 1, arr.length);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner1, arr1);
        category.setAdapter(adapter);

        String[] arr2 = {"Price default", "Price increase", "Price decrease"};
        price.setAdapter(new ArrayAdapter<>(getContext(), R.layout.item_spinner1, arr2));
    }

    @Override
    public void onItemClick(View view, int position) {
        Product product = adapter.getProduct(position);
        sharedPreferences = getActivity().getSharedPreferences(SHARE_PRE_NAME,
                Context.MODE_PRIVATE);
        String roleLogin = sharedPreferences.getString("role", null);
        if (roleLogin != null && roleLogin.equalsIgnoreCase("admin")) {
            Intent intent = new Intent(getActivity(), UpdateDeleteProductActivity.class);
            intent.putExtra("product", product);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), DetailProductActivity.class);
            intent.putExtra("product", product);
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        List<Product> list = productSQLiteHelper.getAll();
        adapter.setList(list);
    }
}
