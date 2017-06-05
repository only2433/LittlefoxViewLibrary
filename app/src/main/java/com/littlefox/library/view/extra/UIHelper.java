package com.littlefox.library.view.extra;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Field;

/**
 * View 선언을 자동화 하는데 도움을 주는 Helper Class
 * @author 정재현
 * 
 */
public class UIHelper {
	
	public static View inflateLayout(Context context, Object container,int layoutId) 
	{
		View view = LayoutInflater.from(context).inflate(layoutId, null);
		mapLayout(container, view);
		return view;
	}
	
	public static View inflateLayout(Context context, int layoutId) 
	{
		View view = LayoutInflater.from(context).inflate(layoutId, null);
		mapLayout(context, view);
		return view;
	}

	/**
	 * View mapping 을 자동화 해주는 Method
	 * @param container
	 * @param view
	 * @return
	 */
	public static View mapLayout(Object container, View view) 
	{
		View view0 = null;
		Field[] fields = container.getClass().getDeclaredFields();
		for (Field field : fields) {
			DeclareView param = field.getAnnotation(DeclareView.class);
			if (param != null) { // To map R.id.xx into field value
				int id = param.id();
				view0 = view.findViewById(id);
				try {
					field.setAccessible(true);
					field.set(container, view0);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// Click Listener
				String clk = param.click().trim();
				if (clk.length() > 0) {
					if (clk.equals("this") == true) {
						view0.setOnClickListener((View.OnClickListener) container);
					} else {
						try {
							Field field0 = container.getClass().getField(clk);
							view0.setOnClickListener((View.OnClickListener) field0
									.get(container));
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}
			}
		}
		return view;
	}

	/**
	 * 해당 부분은 Annotation을 거치지 않고 자동화 할수 있는 메소드다. 대신 Resource ID 와 View 이름은 같아야한다.
	 * @param object
	 */
	public static void mappingViews(Object object) 
	{
		if (!(object instanceof Activity))
			return;

		Activity activity = (Activity) object;
		Field[] fields = activity.getClass().getDeclaredFields();
		for (Field field : fields) {
			String identifierString = field.getName();
			int identifier = activity.getResources().getIdentifier(
					identifierString, "id", activity.getPackageName());
			if (identifier == 0)
				continue;

			View findedView = activity.findViewById(identifier);
			if (findedView == null)
				continue;

			if (findedView.getClass() == field.getType()) {
				try {
					field.setAccessible(true);
					field.set(object, findedView);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
