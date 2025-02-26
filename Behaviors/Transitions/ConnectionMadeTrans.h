//-*-c++-*-
#ifndef INCLUDED_ConnectionMadeTrans_h_
#define INCLUDED_ConnectionMadeTrans_h_

#include "Behaviors/Transition.h"
#include "Events/EventRouter.h"
#include "Wireless/Wireless.h"

//! a transition that occurs as soon as a connection is made
class ConnectionMadeTrans : public Transition {
public:

  //! constructor
  ConnectionMadeTrans(StateNode* destination, Socket* sock) : Transition("ConnectionMadeTrans",destination), socket(sock) {}
	
  //! constructor
  ConnectionMadeTrans(const std::string& name, StateNode* destination, Socket* sock) : 
    Transition("ConnectionMadeTrans",name,destination), socket(sock) {}
	
  //!starts 32 msec timer with sid=0
  virtual void DoStart() {
    Transition::DoStart();
    erouter->addTimer(this,0,32,true);
  }

  //!stops timer
  virtual void DoStop() { erouter->removeListener(this); Transition::DoStop(); }

  //!when timer event is received, fire() the transition
  virtual void processEvent(const EventBase&) { 
    if(socket!=NULL && wireless->isConnected(socket->sock)) {
      fire();
    }
  }

protected:
  //! constructor, only to be called by subclasses (which need to specify their own @a classname)
  ConnectionMadeTrans(const std::string &classname, const std::string &instancename, StateNode* destination, Socket* sock) :
    Transition(classname,instancename,destination), socket(sock) {}
  ConnectionMadeTrans(const ConnectionMadeTrans&);     //!< DON'T CALL THIS
  ConnectionMadeTrans& operator=(const ConnectionMadeTrans&);  //!< DON'T CALL THIS
  


  Socket* socket; //!< the socket to expect the connection on
};

/*! @file
 * @brief Defines ConnectionMadeTrans, which causes a transition as soon as a connection is made
 * @author klibby (Creator)
 *
 * $Author: ejt $
 * $Name: tekkotsu-4_0-branch $
 * $Revision: 1.3 $
 * $State: Exp $
 * $Date: 2005/08/07 04:11:03 $
 */

#endif
