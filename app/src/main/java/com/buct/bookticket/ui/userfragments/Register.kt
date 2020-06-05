package com.buct.bookticket.ui.userfragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController

import com.buct.bookticket.R
import com.buct.bookticket.util.Post
import com.buct.bookticket.util.encode
import org.json.JSONObject

class Register : Fragment() {

    companion object {
        fun newInstance() = Register()
    }

    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.register_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        val button=view?.findViewById<Button>(R.id.button4)
        val button1=view?.findViewById<Button>(R.id.button5)
        val user=view?.findViewById<EditText>(R.id.editText)
        val psw=view?.findViewById<EditText>(R.id.editText2)
        val repsw=view?.findViewById<EditText>(R.id.editText5)
        button?.setOnClickListener {
            if(user?.text.toString()?.equals("")||
                psw?.text.toString()?.equals("")||
                repsw?.text.toString()?.equals("")
                ||psw?.text.toString()?.equals(repsw?.text.toString())==false){
                Toast.makeText(activity,"请输入或两次密码输入不同",Toast.LENGTH_SHORT).show()
            }else{
                Thread(Runnable {
                    var object1=JSONObject()
                    object1.put("name",user?.text.toString())
                    object1.put("psw", encode(psw?.text.toString()))
                    val res=Post(requireContext(),"http://49.233.81.150:9090/service/register",object1.toString())
                    activity?.runOnUiThread(Runnable {
                        if(res.equals("-1")){
                            Toast.makeText(activity,"已经注册过了",Toast.LENGTH_SHORT).show()
                        }else if(res.equals("0")){
                            Toast.makeText(activity,"注册失败",Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(activity,"注册成功！",Toast.LENGTH_SHORT).show()
                            view?.findNavController()?.navigate(R.id.action_register_to_loginFragment)
                        }
                    })
                }).start()
            }
        }
        button1?.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_register_to_loginFragment)
        }
        // TODO: Use the ViewModel
    }

}
