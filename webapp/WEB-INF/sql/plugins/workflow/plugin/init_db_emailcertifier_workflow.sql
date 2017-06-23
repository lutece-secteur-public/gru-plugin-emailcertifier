-- Initialize Workflow for plugin EmailCertifier

INSERT INTO workflow_workflow (id_workflow, name, description, creation_date, is_enabled, workgroup_key) 
	VALUES	(700,'Workflow Certification Email','Workflow Certification Email','2016-01-13 08:36:34',1,'all');

INSERT INTO workflow_state (id_state, name, description, id_workflow, is_initial_state, is_required_workgroup_assigned, id_icon, display_order) 
	VALUES	(801,'Initial','Certification demandée par usager',700,1,0,NULL,1),
                (802,'En cours de certification','A traiter',700,0,0,NULL,2),
                (803,'Certifié','Adresse email certifiée',700,0,0,NULL,3),
                (804,'Certification abandonnée','Certification annulée',700,0,0,NULL,4);

INSERT INTO workflow_action (id_action, name, description, id_workflow, id_state_before, id_state_after, id_icon, is_automatic, is_mass_action, display_order, is_automatic_reflexive_action) 
	VALUES 	(901,'Initialisation','Initialisation de la demande de certification',700,801,802,1,1,0,1,0),
                (902,'Certifier adresse email','Lancer la certification de adresse email',700,802,803,1,0,0,2,0),
                (903,'Invalider la certification','Invalider la certification de adresse email',700,802,804,1,0,0,3,0);

INSERT INTO workflow_task (id_task, task_type_key, id_action, display_order) 
	VALUES 	(1001,'taskNotifyGru',901,1),
                (1002,'taskValidateEmailCertificationTask',902,1),
		(1003,'taskNotifyGru',903,1);

INSERT INTO workflow_task_notify_gru_cf (id_task,id_spring_provider,demand_status,crm_status_id,set_onglet,message_guichet,status_text_guichet,sender_name_guichet,subject_guichet,demand_max_step_guichet,demand_user_current_step_guichet,is_active_onglet_guichet,status_text_agent,message_agent,is_active_onglet_agent,subject_email,message_email,sender_name_email,recipients_cc_email,recipients_cci_email,is_active_onglet_email,message_sms,is_active_onglet_sms,id_mailing_list_broadcast,email_broadcast,sender_name_broadcast,subject_broadcast,message_broadcast,recipients_cc_broadcast,recipients_cci_broadcast,is_active_onglet_broadcast) 
VALUES (1,'emailcertifier.certifiableemail.provider-manager.@.certifiable_email_provider',1,1,0,NULL,NULL,NULL,NULL,0,0,0,NULL,NULL,0,'Certifier votre adresse mail','<p>${(certification_link?html)!}</p>','Mairie de Paris','','',1,NULL,0,0,NULL,NULL,NULL,NULL,NULL,NULL,0);
