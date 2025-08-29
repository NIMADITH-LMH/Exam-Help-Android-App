package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.fragment.app.Fragment

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var dotsLayout: LinearLayout
    private lateinit var btnNext: Button
    private lateinit var btnSkip: TextView

    private val onboardingItems = listOf(
        OnboardingItem(
            "Interactive Quizzes & Tests",
            "Test your knowledge with our interactive quizzes to reinforce your learning.",
            R.drawable.ic_onboarding_1
        ),
        OnboardingItem(
            "HD Video Lessons",
            "Learn with high-quality video content from expert educators.",
            R.drawable.ic_onboarding_2
        ),
        OnboardingItem(
            "Push Notifications & Reminders",
            "Stay on track with timely reminders and updates about your courses.",
            R.drawable.ic_onboarding_3
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        // Set up ViewPager
        viewPager = findViewById<ViewPager2>(R.id.onboardingViewPager)
        dotsLayout = findViewById(R.id.dotIndicator)
        btnNext = findViewById(R.id.btnNext)
        btnSkip = findViewById(R.id.btnSkip)
        
        viewPager.adapter = OnboardingFragmentAdapter(onboardingItems, this)
        
        // Set up dots indicator
        setUpDotIndicator()
        
        // Update dots on page change
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDots(position)
                
                // If last page, change button text to "Get Started"
                if (position == onboardingItems.size - 1) {
                    btnNext.text = "Get Started"
                } else {
                    btnNext.text = "Next"
                }
            }
        })
        
        // Set up button click listeners
        btnNext.setOnClickListener {
            if (viewPager.currentItem < onboardingItems.size - 1) {
                // Go to next page
                viewPager.currentItem++
            } else {
                // Go to sign in screen
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            }
        }
        
        btnSkip.setOnClickListener {
            // Skip to sign in screen
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }
    
    private fun setUpDotIndicator() {
        val dots = arrayOfNulls<View>(onboardingItems.size)

        for (i in 0 until onboardingItems.size) {
            dots[i] = View(this)
            dots[i]?.layoutParams = LinearLayout.LayoutParams(12, 12).apply {
                setMargins(8, 0, 8, 0)
            }
            dots[i]?.background = ContextCompat.getDrawable(this, R.drawable.dot_inactive)
            dotsLayout.addView(dots[i])
        }
    }
    
    private fun updateDots(position: Int) {
        for (i in 0 until dotsLayout.childCount) {
            val dot = dotsLayout.getChildAt(i)
            dot.background = ContextCompat.getDrawable(
                this,
                if (i == position) R.drawable.dot_active else R.drawable.dot_inactive
            )
        }
    }
}

// ViewPager Adapter
class OnboardingFragmentAdapter(private val items: List<OnboardingItem>, activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    
    override fun getItemCount(): Int = items.size
    
    override fun createFragment(position: Int): Fragment {
        return OnboardingFragment.newInstance(items[position])
    }
}

// Fragment for each onboarding page
class OnboardingFragment : Fragment() {
    
    companion object {
        private const val ARG_ONBOARDING_ITEM = "onboarding_item"
        
        fun newInstance(item: OnboardingItem): OnboardingFragment {
            val fragment = OnboardingFragment()
            val args = Bundle().apply {
                // Using putSerializable instead of putParcelable since we're not implementing Parcelable
                putSerializable(ARG_ONBOARDING_ITEM, item)
            }
            fragment.arguments = args
            return fragment
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.onboarding_page, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Use getSerializable for newer Android versions
        @Suppress("DEPRECATION")
        val item = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(ARG_ONBOARDING_ITEM, OnboardingItem::class.java)
        } else {
            arguments?.getSerializable(ARG_ONBOARDING_ITEM) as? OnboardingItem
        }
        
        item?.let {
            view.findViewById<ImageView>(R.id.ivOnboarding).setImageResource(it.imageRes)
            view.findViewById<TextView>(R.id.tvOnboardingTitle).text = it.title
            view.findViewById<TextView>(R.id.tvOnboardingDesc).text = it.description
        }
    }
}

// Using OnboardingItem from separate file
