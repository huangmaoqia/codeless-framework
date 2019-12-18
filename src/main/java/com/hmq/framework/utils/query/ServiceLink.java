package com.hmq.framework.utils.query;

import com.hmq.framework.service.impl.GenService;

public class ServiceLink {
	private ServiceLink previous=null;
	private GenService<?,?,?> service=null;
	private ServiceLink next=null;
	
	public ServiceLink getNext(){
		return this.next;
	}
	public ServiceLink getPrevious(){
		return this.previous;
	}
	
	public GenService<?,?,?> getValue(){
		return this.service;
	}
	
	public ServiceLink(GenService<?,?,?> service){
		this.service=service;
//		this.previous=this;
//		this.next=this;
	}
	
	public ServiceLink add(GenService<?,?,?> service){
		ServiceLink newNext=new ServiceLink(service);
		ServiceLink previous=this.previous;
		if(previous!=null){
			previous.next=newNext;
			newNext.next=this;
			newNext.previous=previous;
			this.previous=newNext;
		}else{
			this.previous=newNext;
			this.next=newNext;
			newNext.next=this;
			newNext.previous=this;
		}
		return this;
	}
}
