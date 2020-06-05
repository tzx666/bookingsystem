package com.buct.bookticket.ui.userfragments

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController

import com.buct.bookticket.R
import com.buct.bookticket.util.encode
import kotlin.math.log

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel
    private lateinit var editname:EditText
    private lateinit var editpsw:EditText
    private lateinit var login:Button
    private lateinit var register: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        editname=view?.findViewById(R.id.editText3) as EditText
        editpsw=view?.findViewById(R.id.editText4) as EditText
        login=view?.findViewById(R.id.button2) as Button
        register=view?.findViewById(R.id.button3) as Button
        register.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_loginFragment_to_register)
        }
        login.setOnClickListener {
            Log.d("login", editname.text.toString()+editpsw.text.toString()+ encode(editpsw.text.toString()))
            if(editname.text.length<2||editpsw.text.length<5){
                Toast.makeText(requireContext(),"请先输入",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.get(editname.text.toString(), encode(editpsw.text.toString()), activity as Context)
                .observe(viewLifecycleOwner, Observer<String> {
                    res->
                    Log.d("login", "登录结果"+res)
                    if(res.equals("1")){
                        Toast.makeText(requireContext(),"登录成功",Toast.LENGTH_SHORT).show()
                        if(view?.findNavController()?.currentDestination?.id==R.id.loginFragment)
                        view?.findNavController()?.navigate(R.id.action_loginFragment_to_navigation_home)
                    }else{
                        Toast.makeText(requireContext(),"登录失败",Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

}
