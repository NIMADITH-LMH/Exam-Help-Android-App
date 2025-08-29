package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PaymentAdapter(private val items: List<PaymentItem>) : RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_payment, parent, false)
        return PaymentViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class PaymentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCourseTitle: TextView = itemView.findViewById(R.id.tvCourseTitle)
        private val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvPaymentMethod: TextView = itemView.findViewById(R.id.tvPaymentMethod)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)

        fun bind(payment: PaymentItem) {
            tvCourseTitle.text = payment.courseTitle
            tvAmount.text = payment.amount
            tvDate.text = payment.date
            tvPaymentMethod.text = payment.paymentMethod
            tvStatus.text = payment.status
            
            // Change status text color based on status
            when (payment.status) {
                "Completed" -> tvStatus.setTextColor(itemView.context.getColor(R.color.colorSuccess))
                "Pending" -> tvStatus.setTextColor(itemView.context.getColor(R.color.colorWarning))
                "Failed" -> tvStatus.setTextColor(itemView.context.getColor(R.color.colorError))
            }
        }
    }
}
